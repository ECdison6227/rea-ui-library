const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const acorn = require('/Users/cjr/.npm/_npx/0aa74679bec75e15/node_modules/acorn');
const postcss = require('/Users/cjr/.npm/_npx/110f701c48e68d66/node_modules/postcss');
const fontkit = require('/Users/cjr/.npm/_npx/110f701c48e68d66/node_modules/fontkit');
const root = path.resolve(__dirname, '..');
const parent = path.dirname(root);
const acquisition = JSON.parse(fs.readFileSync(path.join(root, 'acquisition.json')));
const historical = JSON.parse(fs.readFileSync(path.join(parent, 'historical-app-assets.json')));
const publicFonts = JSON.parse(fs.readFileSync(path.join(parent, 'public/font-payload-inspection.json')));
const hash = b => crypto.createHash('sha256').update(b).digest('hex');
const write = (name, data) => fs.writeFileSync(path.join(root, name), JSON.stringify(data, null, 2) + '\n');
const range = (text, start, end) => ({ utf16CodeUnits: { start, endExclusive: end }, utf8Bytes: { start: Buffer.byteLength(text.slice(0, start)), endExclusive: Buffer.byteLength(text.slice(0, end)) } });
const source = (item, text, node) => ({ path: item.path, url: item.url, sha256: item.sha256, range: range(text, node.start, node.end) });
function walk(node, visit, ancestors = []) {
  if (!node || typeof node !== 'object') return;
  if (node.type) visit(node, ancestors);
  for (const value of Object.values(node)) if (Array.isArray(value)) value.forEach(n => walk(n, visit, [...ancestors, node])); else if (value && typeof value === 'object') walk(value, visit, [...ancestors, node]);
}
const scalarProps = obj => obj?.type === 'ObjectExpression' ? obj.properties.map(p => ({ key: p.key?.name ?? p.key?.value ?? p.type, kind: p.value?.type ?? p.type, ...(p.value?.type === 'Literal' ? { value: p.value.value } : {}) })) : [];
const glyphs = [], svgFactories = [], themeBindings = [], skyBindings = [], tokenScopes = [], cssFontFaces = [], cssUrls = [], parseErrors = [];
const owners = new Map();
for (const item of acquisition.items.filter(i => i.available && i.path.endsWith('.js'))) {
  const text = fs.readFileSync(path.join(root, item.path), 'utf8');
  let ast;
  try { ast = acorn.parse(text, { ecmaVersion: 'latest', sourceType: 'module' }); } catch (e) { parseErrors.push({ path: item.path, error: e.message }); continue; }
  const exports = new Map();
  for (const n of ast.body) if (n.type === 'ExportNamedDeclaration') for (const s of n.specifiers || []) { const a = exports.get(s.local.name) || []; a.push(s.exported.name); exports.set(s.local.name, a); }
  function keepOwner(node, parents) {
    const owner = [...parents].reverse().find(n => /Function(?:Declaration|Expression)$|ArrowFunctionExpression/.test(n.type)) || node;
    const parentNode = parents[parents.indexOf(owner) - 1];
    const name = owner.id?.name || (parentNode?.type === 'VariableDeclarator' ? parentNode.id?.name : null);
    const id = item.path + ':' + owner.start + ':' + owner.end;
    if (!owners.has(id)) owners.set(id, { id, name, exportedAs: exports.get(name) || [], source: source(item, text, owner), bytes: Buffer.byteLength(text.slice(owner.start, owner.end)), rawSource: owner.end - owner.start <= 100000 ? text.slice(owner.start, owner.end) : null, rawSourceOmittedReason: owner.end - owner.start > 100000 ? 'Large owner retained exactly in original resource; source range remains authoritative.' : null });
    return id;
  }
  walk(ast, (n, parents) => {
    if (n.type === 'ObjectExpression') {
      const props = n.properties.filter(p => p.type === 'Property');
      if (props.length >= 50 && props.some(p => p.value.type === 'Literal' && typeof p.value.value === 'string' && p.value.value.startsWith('<path'))) {
        const parentNode = parents.at(-1), name = parentNode?.type === 'VariableDeclarator' ? parentNode.id.name : null;
        for (const p of props) {
          if (p.value.type !== 'Literal' || typeof p.value.value !== 'string') continue;
          const glyphName = p.key.name ?? p.key.value, body = p.value.value;
          const old = historical.glyphRegistry.find(g => g.name === glyphName);
          glyphs.push({ name: glyphName, registrySymbol: name, sourceLiteral: source(item, text, p.value), rawLiteral: text.slice(p.value.start, p.value.end), decodedBody: body, decodedBodySha256: hash(Buffer.from(body)), defaultWrapper: { viewBox: '0 0 16 16', width: 16, height: 16, fill: 'none', status: 'B geometry from named renderer; original dynamic size/paint props remain in source' }, svg: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">${body}</svg>`, historicalSameDecodedBody: !!old && old.literalBodySha256 === hash(Buffer.from(body)), classification: 'B source geometry; standalone SVG wrapper is derived serialization; current state binding not established' });
        }
      }
    }
    if (n.type === 'CallExpression' && n.arguments[0]?.type === 'Literal' && n.arguments[0].value === 'svg') {
      const ownerId = keepOwner(n, parents), owner = owners.get(ownerId);
      const rootProps = scalarProps(n.arguments[1]);
      const shapes = [];
      walk(n.arguments[1], shape => { if (shape.type === 'CallExpression' && ['path','rect','circle','ellipse','line','polyline','polygon','g','defs','clipPath','mask','use','linearGradient','stop'].includes(shape.arguments[0]?.value)) shapes.push({ tag: shape.arguments[0].value, props: scalarProps(shape.arguments[1]), source: source(item, text, shape), rawSource: text.slice(shape.start, shape.end) }); });
      svgFactories.push({ ownerId, source: source(item, text, n), rootProps, rawSource: text.slice(n.start, n.end), literalShapeCallsInsideRootExpression: shapes, scope: item.path.endsWith('main-BgE0mqJD.js') ? 'current-app-main' : 'archived-app-or-vendor-module; ownership not inferred solely from SVG syntax', classification: 'B complete exact source owner retained; dynamic cache/children/branch values require original scope' });
    }
    if (n.type === 'Literal' && typeof n.value === 'string' && n.value === 'theme-light') {
      const ownerId = keepOwner(n, parents); themeBindings.push({ value: n.value, source: source(item, text, n), ownerId });
    }
    if (n.type === 'VariableDeclarator' && n.id.type === 'Identifier' && n.init && ['ObjectExpression','NewExpression'].includes(n.init.type)) {
      const raw = text.slice(n.start, n.end);
      if (raw.includes('wallpaperUrl') && (raw.includes('selfBoot') || raw.length < 1500)) skyBindings.push({ symbol: n.id.name, kind: n.init.type, source: source(item, text, n), rawSource: raw, classification: 'B current original sky loader/config; fetching or execution not performed' });
    }
    if (item.path.endsWith('BbFz-0Fv.js') && n.type === 'FunctionDeclaration') {
      const raw = text.slice(n.start, n.end);
      if (raw.includes('.setForcedHour') || raw.includes('.theme') || raw.includes('choice:') || raw.includes('.getHost') || raw.includes('.isPainted')) skyBindings.push({ symbol: n.id?.name, exportedAs: exports.get(n.id?.name) || [], source: source(item, text, n), rawSource: raw, classification: 'B current sky/theme React wrapper' });
    }
  });
}
for (const item of acquisition.items.filter(i => i.available && i.path.endsWith('.css'))) {
  const text = fs.readFileSync(path.join(root, item.path), 'utf8');
  const ast = postcss.parse(text, { from: item.url });
  function cssSource(n) { return { path: item.path, url: item.url, sha256: item.sha256, range: range(text, n.source.start.offset, n.source.end.offset + 1) }; }
  ast.walkRules(rule => {
    const custom = rule.nodes.filter(n => n.type === 'decl' && n.prop.startsWith('--'));
    if (!custom.length) return;
    const conditions = []; let p = rule.parent;
    while (p && p.type !== 'root') { if (p.type === 'atrule') conditions.unshift({ name: p.name, params: p.params }); p = p.parent; }
    tokenScopes.push({ selector: rule.selector, conditions, source: cssSource(rule), rawSource: text.slice(rule.source.start.offset, rule.source.end.offset + 1), declarations: custom.map(d => ({ name: d.prop, value: d.value, important: d.important, source: cssSource(d), rawSource: text.slice(d.source.start.offset, d.source.end.offset + 1) })) });
  });
  ast.walkAtRules('font-face', rule => cssFontFaces.push({ source: cssSource(rule), rawSource: text.slice(rule.source.start.offset, rule.source.end.offset + 1), properties: Object.fromEntries(rule.nodes.filter(n => n.type === 'decl').map(n => [n.prop,n.value])) }));
  ast.walkDecls(d => { if (/\burl\(/.test(d.value)) cssUrls.push({ property: d.prop, value: d.value, source: cssSource(d), classification: 'B inert CSS declaration; URL expressions not fetched except font-face above' }); });
}
const fonts = [];
for (const item of acquisition.items.filter(i => i.available && i.path.startsWith('fonts/'))) {
  const bytes = fs.readFileSync(path.join(root, item.path));
  const font = fontkit.create(bytes), coverage = font.characterSet.filter(c => font.glyphForCodePoint(c).id !== 0);
  const os2 = font['OS/2'], publicMatches = publicFonts.items.filter(p => p.sha256 === item.sha256);
  fonts.push({ path: item.path, url: item.url, bytes: item.bytes, sha256: item.sha256, header: { signature: bytes.subarray(0,4).toString(), declaredLength: bytes.readUInt32BE(8), completeLength: bytes.readUInt32BE(8) === bytes.length }, names: { family: font.familyName, subfamily: font.subfamilyName, fullName: font.fullName, postScriptName: font.postscriptName }, metrics: { unitsPerEm: font.unitsPerEm, ascent: font.ascent, descent: font.descent, lineGap: font.lineGap, xHeight: font.xHeight, capHeight: font.capHeight }, payloadWeight: os2?.usWeightClass, italicAngle: font.italicAngle, numGlyphs: font.numGlyphs, rawCharacterSetCount: font.characterSet.length, effectiveCodepoints: coverage.length, effectiveCodepointSet: coverage, codepointSetSha256: hash(Buffer.from(JSON.stringify(coverage))), variationAxes: font.variationAxes || {}, publicByteMatches: publicMatches.map(p => ({ path: 'public/' + p.path, sha256: p.sha256, cssBinding: p.cssBinding })), cssBindings: acquisition.fontFaces.filter(f => f.urls.includes(item.url)).map(f => ({ sourcePath: f.sourcePath, sourceSha256: f.sourceSha256, sourceRange: f.sourceRange, family: f.properties['font-family'], weight: f.properties['font-weight'], style: f.properties['font-style'], display: f.properties['font-display'] })), classification: 'B newly acquired current bytes + fresh inert fontkit2.0.4 metadata; no historical byte-time claim' });
}
write('font-inventory.json', { schemaVersion:1, parser:{name:'fontkit',version:'2.0.4',method:'read-only original WOFF2 metadata; no install or runtime binding'}, faces:cssFontFaces, items:fonts, summary:{cssFaces:cssFontFaces.length,files:fonts.length,totalBytes:fonts.reduce((a,b)=>a+b.bytes,0),filesIdenticalToPublic:fonts.filter(f=>f.publicByteMatches.length).length,filesAbsentFromPublic:fonts.filter(f=>!f.publicByteMatches.length).map(f=>f.path)}, historicalBoundary:'Current URLs newly obtained successfully; identical historical CSS declaration URL does not establish the bytes served on2026-10-15.' });
write('icons.json', { schemaVersion:1, summary:{namedGlyphs:glyphs.length,historicalNamedBodyMatches:glyphs.filter(g=>g.historicalSameDecodedBody).length,svgRoots:svgFactories.length,uniqueSourceOwners:new Set(svgFactories.map(i=>i.ownerId)).size}, namedGlyphs:glyphs, svgFactories, owners:[...owners.values()].filter(o=>svgFactories.some(i=>i.ownerId===o.id)), parseErrors, limits:['Vendor and App declarations remain separately scoped; SVG syntax alone does not prove a visible current control.','Cached/dynamic factory props are retained exactly; no target code was evaluated or flattened.'] });
fs.writeFileSync(path.join(root,'named-glyph-sprite.svg'),'<svg xmlns="http://www.w3.org/2000/svg" fill="none">'+glyphs.map(g=>`<symbol id="hark-${g.name}" viewBox="0 0 16 16">${g.decodedBody}</symbol>`).join('')+'</svg>\n');
write('theme-tokens.json',{schemaVersion:1,classification:'B source scope and conditions; active current theme belongs to coordinating runtime evidence',summary:{rules:tokenScopes.length,customPropertyDeclarations:tokenScopes.reduce((a,s)=>a+s.declarations.length,0),uniqueCustomProperties:new Set(tokenScopes.flatMap(s=>s.declarations.map(d=>d.name))).size},scopes:tokenScopes,themeLightBindings:themeBindings.map(b=>({...b,owner:owners.get(b.ownerId)})),cssUrlDeclarations:cssUrls});
write('sky-static.json',{schemaVersion:1,status:'current loader and theme wrappers retained; wallpaper HTTP403 prevents current shader/LUT identity',sources:skyBindings,wallpaperAcquisition:'wallpaper-acquisition.json',historicalShaderBoundary:'H15 shader/source/LUT already retained elsewhere remains historical B. No byte identity claim is made without current wallpaper HTML.',excludedFalsePositive:{path:'resources/C8Q_188t.js',detail:'GLSL terms in static syntax-highlighter grammar are not sky shader code.'}});
write('extraction-summary.json',{schemaVersion:1,generatedAt:new Date().toISOString(),rawArchive:acquisition.summary,fonts:fonts.length,publicFontByteMatches:fonts.filter(f=>f.publicByteMatches.length).length,namedGlyphs:glyphs.length,oldGlyphGeometryMatches:glyphs.filter(g=>g.historicalSameDecodedBody).length,svgRoots:svgFactories.length,svgOwners:new Set(svgFactories.map(i=>i.ownerId)).size,themeRules:tokenScopes.length,customProperties:new Set(tokenScopes.flatMap(s=>s.declarations.map(d=>d.name))).size,skySourceEntries:skyBindings.length,parseErrors});
process.stdout.write(JSON.stringify(JSON.parse(fs.readFileSync(path.join(root,'extraction-summary.json'))))+'\n');
