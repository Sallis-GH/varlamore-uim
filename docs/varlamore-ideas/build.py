"""Assemble docs/varlamore-ideas/index.html from the template and the JSON content files.

Layout of inputs (all relative to this directory):
  content/overview.json            {title, lead, blocks:[{title, html}]}
  content/unlock.json              {title, lead, intro_html, candidates:[{id,name,type,location,why,pros,cons,source_html,script?}]}
  content/plugin.json              {sections:[{id,title,lead,intro_html,items:[{id,title,summary,html,source_html}]}]}
  content/characters.json          {title, lead, characters:[{id,name,dock,brief}]}
  scripts/<character-id>/*.json    writer outputs: {character, author, variants:[{title, phase, premise?, lines:[{speaker,text}], options:[{label, lines:[...]}]}]}

Every scoreable element gets a stable id derived from file names and positions, so scores
survive rebuilds as long as the ordering of variants inside a writer file does not change.
"""
import json
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))


def load(path):
    with open(os.path.join(HERE, path), encoding='utf-8') as f:
        return json.load(f)


def slug(s):
    return re.sub(r'[^a-z0-9]+', '-', s.lower()).strip('-')


def check_line(text, where):
    if len(text) > 160:
        print(f'WARN {where}: line over 160 chars ({len(text)}): {text[:60]}...', file=sys.stderr)


def collect_scripts(char_id):
    d = os.path.join(HERE, 'scripts', char_id)
    variants = []
    if not os.path.isdir(d):
        return variants
    for fn in sorted(os.listdir(d)):
        if not fn.endswith('.json'):
            continue
        data = load(os.path.join('scripts', char_id, fn))
        author = data.get('author') or fn[:-5]
        for i, v in enumerate(data.get('variants', [])):
            vid = f"{char_id}:{slug(author)}:{i}"
            for l in v.get('lines', []):
                check_line(l['text'], vid)
            for o in v.get('options', []):
                for l in o.get('lines', []):
                    check_line(l['text'], vid)
            variants.append({
                'id': vid,
                'title': v.get('title', f'Variant {i+1}'),
                'author': author,
                'phase': v.get('phase', 'pre'),
                'premise': v.get('premise', ''),
                'lines': v.get('lines', []),
                'options': v.get('options', []),
            })
    # stable order: pre first, then post; within a phase: editor's pick, approved, writers, reviewer
    def rank(author):
        a = author.lower()
        if 'editor' in a:
            return 0
        if 'approved' in a:
            return 1
        if 'writer' in a:
            return 2
        return 3
    variants.sort(key=lambda v: (0 if v['phase'] == 'pre' else 1, rank(v['author'])))
    return variants


def main():
    overview = load('content/overview.json')
    unlock = load('content/unlock.json')
    # merge the unlocker exchanges written separately
    if os.path.exists(os.path.join(HERE, 'content/unlocker-scripts.json')):
        scripts = {c['id']: c for c in load('content/unlocker-scripts.json')['candidates']}
        for c in unlock['candidates']:
            s = scripts.get(c['id'])
            if s:
                c['script'] = s.get('with')
                c['script_without'] = s.get('without')
            else:
                print(f'WARN no unlocker script for {c["id"]}', file=sys.stderr)
    plugin = load('content/plugin.json')
    chars = load('content/characters.json')

    characters = []
    for ch in chars['characters']:
        variants = collect_scripts(ch['id'])
        if not variants:
            print(f'WARN no scripts for {ch["id"]}', file=sys.stderr)
        characters.append({**ch, 'variants': variants})

    data = {
        'meta': {'title': 'Varlamore UIM Ideas', 'subtitle': 'Dialogue drafts and plugin proposals to score'},
        'overview': overview,
        'unlock': unlock,
        'characters_title': chars['title'],
        'characters_lead': chars['lead'],
        'characters': characters,
        'plugin': plugin,
    }
    with open(os.path.join(HERE, 'index.template.html'), encoding='utf-8') as f:
        tpl = f.read()
    payload = json.dumps(data, ensure_ascii=False).replace('</', '<\\/')
    out = tpl.replace('/*__DATA__*/', payload)
    with open(os.path.join(HERE, 'index.html'), 'w', encoding='utf-8', newline='\n') as f:
        f.write(out)
    total_lines = sum(len(v['lines']) + sum(1 + len(o['lines']) for o in v['options']) for c in characters for v in c['variants'])
    print(f'built index.html: {len(characters)} characters, '
          f'{sum(len(c["variants"]) for c in characters)} variants, {total_lines} scoreable lines, '
          f'{len(unlock["candidates"])} unlock candidates, '
          f'{sum(len(s["items"]) for s in plugin["sections"])} plugin ideas')


if __name__ == '__main__':
    main()
