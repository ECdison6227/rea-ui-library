from pathlib import Path
from PIL import Image
import json

base = Path('analysis/ui/runtime')
results = {}
for path in (base / 'captures').glob('*/screen.png'):
    with Image.open(path) as img:
        extrema = img.convert('RGB').getextrema()
        uniform = all(low == high for low, high in extrema)
        black = all(low == high == 0 for low, high in extrema)
        results[path.parent.name] = {
            'path': str(path), 'width': img.width, 'height': img.height,
            'extrema': extrema, 'uniform': uniform, 'black': black,
            'usableForVisualAnalysis': not uniform,
            'note': 'Uniform frame excluded from visual claims; hierarchy may remain available.' if uniform else 'Non-uniform original capture; no image edits performed.'
        }
(base / 'screen-quality.json').write_text(json.dumps(results, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps({'frames': len(results), 'excluded': [k for k,v in results.items() if not v['usableForVisualAnalysis']]}, ensure_ascii=False))
