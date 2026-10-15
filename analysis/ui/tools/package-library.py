"""Package the portable UI library; never embeds tools, debug keys or build staging."""
import hashlib,json,zipfile
from pathlib import Path

workspace=Path(__file__).resolve().parents[3]
ui=workspace/'analysis/ui'
output=workspace/'analysis/xiaomi-ui-ux-spec.zip'
manifest=workspace/'analysis/xiaomi-ui-ux-spec.manifest.json'
def sha(data):return hashlib.sha256(data).hexdigest()
files=[workspace/name for name in ['README.md','AGENTS.md','.gitignore','.gitattributes']]
for item in sorted(ui.rglob('*')):
    if not item.is_file():continue
    relative=item.relative_to(ui).as_posix()
    if '/.local/' in '/'+relative or '__pycache__' in item.parts or item.suffix=='.pyc':continue
    if relative.startswith('android-reference/build/') and item.name not in {'style-reference.apk','build-info.json','build.log'}:continue
    files.append(item)
entries=[]
with zipfile.ZipFile(output,'w',compression=zipfile.ZIP_DEFLATED,compresslevel=6,allowZip64=True) as archive:
    for item in files:
        member=item.relative_to(workspace).as_posix()
        data=item.read_bytes();archive.writestr(member,data)
        entries.append({'path':member,'bytes':len(data),'sha256':sha(data)})
with zipfile.ZipFile(output) as archive:
    assert archive.testzip() is None
    for entry in entries:assert sha(archive.read(entry['path']))==entry['sha256'],entry['path']
    names=set(archive.namelist())
    for required in ['analysis/ui/library.json','analysis/ui/baseline/coverage.json','analysis/ui/baseline/popup-navigation.json','analysis/ui/baseline/comparison/final/index.html','analysis/ui/android-reference/build/style-reference.apk','analysis/ui/asset-library/inventory.json','analysis/ui/docs/next-agent-prompt.md']:
        assert required in names,required
    assert not any('.local/' in name or name.endswith('.keystore') for name in names)
result={'generatedAt':'see baseline verification timestamps','archive':output.name,'bytes':output.stat().st_size,'sha256':sha(output.read_bytes()),'entries':entries,'verifiedEveryMemberHash':True,'exclusions':['original base.apk kept unchanged in workspace','tool binaries and local SDK','debug signing keys','.local smoke projects','intermediate build work'],'meaning':'Packaging integrity only, not design fidelity. See baseline/coverage.json.'}
manifest.write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps({key:result[key] for key in ['archive','bytes','sha256','verifiedEveryMemberHash']},ensure_ascii=False))
