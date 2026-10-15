from pathlib import Path
from PIL import Image
from collections import Counter
import json

base = Path('analysis/ui/runtime')
rules = {
    '13-app-settings': {'floor': (10, 800), 'primary_button': (250, 2200)},
    '14-settings-night': {'floor': (10, 800), 'primary_button': (250, 2200)},
    '06-entry-after-region': {'navigation_floor': (10, 2300)},
    '15-health-night': {'navigation_floor': (10, 2300)},
    '07-sport': {'page_floor': (10, 1200)},
    '18-sport-night': {'page_floor': (10, 1200)},
    '11-mine-visible': {'page_floor': (10, 1450)},
    '17-mine-night': {'page_floor': (10, 1450)},
    '21-mine-day-updated': {'page_floor': (10, 1450)}
}
observations = []
for name, points in rules.items():
    with Image.open(base / 'captures' / name / 'screen.png') as source:
        image = source.convert('RGB')
        result = {'capture': name, 'samples': []}
        for role, (x, y) in points.items():
            rgb = image.getpixel((x, y))
            result['samples'].append({'role': role, 'pointPx': [x, y], 'rgb': rgb, 'hex': '#%02x%02x%02x' % rgb})
        if name in ('06-entry-after-region', '15-health-night'):
            counts = Counter(image.getpixel((x, y)) for y in range(2151, 2334) for x in range(1080))
            result['navRegionTopColors'] = [{'hex': '#%02x%02x%02x' % rgb, 'pixels': count} for rgb, count in counts.most_common(8)]
        observations.append(result)
(base / 'pixel-observations.json').write_text(json.dumps({'method': 'Read-only PNG pixel sampling; no edits or generated screenshots', 'limitations': ['Point samples describe these pixels only, not every surface in an app.', 'Different runtime bundle versions are recorded separately; no cross-version shape equivalence is claimed.'], 'observations': observations}, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps(observations, ensure_ascii=False))
