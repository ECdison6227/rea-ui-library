
#ifdef GL_ES
precision highp float;
#endif
uniform vec2  uRes;
uniform float uDet;      // detail unit in device px = min(height, width / aspect): the size of one 'height unit' for the details
uniform float uTime;     // slow, accumulated time (drives drift)
uniform vec3  uC0;       // zenith
uniform vec3  uC1;
uniform vec3  uC2;
uniform vec3  uC3;       // horizon
uniform vec2  uGlowPos;
uniform vec3  uGlowCol;
uniform float uGlow;
uniform float uGlowSize; // body footprint: >1 near the horizon, 1 high up
uniform float uDensity;
uniform float uWarp;
uniform float uGrain;
uniform vec2  uDrift;    // wind: direction (screen space) x strength
uniform vec3  uPrecip;   // x rain, y snow/hail, z fall-speed multiplier
uniform vec3  uShaft;    // x strength, y coverage, z reach (how far down)
uniform float uFog;      // clumpy fog amount
uniform vec3  uMilk;     // fog color (ambient-light aware)
uniform float uDeck;     // overcast lid amount: drives cloud-base mottle
uniform float uPuff;     // cumulus amount: defined bright clumps (partly regime)
uniform float uNight;    // additive night-cloud veil (dark skies only)
uniform float uHaze;     // atmospheric perspective: textured white low band (day)
uniform float uSquash;   // gradient height compression: <1 squeezes the low stops' span
uniform vec3  uTint;     // low-sun cloud tint: chromatic multiply on bright field material
uniform float uRays;     // godrays: beam-fan strength (rides the glow's visibility)
uniform vec2  uRayLook;  // godrays look: x beam scale, y radial falloff (live-tuned)
uniform float uHalo;     // 22° solar halo amount (frame param, crossfaded)
uniform vec3  uHaloLook; // halo look: x ring radius, y dispersion, z softness (live-tuned)
uniform sampler2D uLut;  // owner color LUT: 33³ as a 1089×33 slice strip
uniform float uLutAmt;   // LUT mix strength (0 = ungraded)
uniform float uTexV;    // global texture vibrancy (grade-shipped)
uniform vec4  uCirA;     // cirrus: base angle (rad), angle variance (rad), fiber density x, opacity
uniform vec4  uCirB;     // cirrus: coverage bias, evolve speed, break depth, grain scale
uniform vec3  uSun;      // THE SUN for sky geometry, whatever body the glow shows: x,y screen position (compositional sweep + bodyY), z TRUE altitude (deg)
uniform float uPolar;    // polarizer band strength (frame param: dial × daylight × clarity × no deck)
uniform vec2  uAureole;  // aureole: x strength (frame param), y half-width in degrees (grows with turbidity)
uniform float uWhiten;   // horizon whitening: aerosol optical depth per air mass (frame param: dial × turbidity × gates)
uniform vec3  uPurple;   // purple light: x strength (frame param), y lobe center altitude (deg), z lobe σ (deg)
uniform vec2  uCrep;     // crepuscular shadow fan: x strength (frame param), y angular noise scale
uniform vec3  uLit;      // cloud lighting: x thin-cloud darkening, y back-light strength, z low-sun airlight bias (frame params)
uniform vec3  uTintHi;   // low-sun tint for the HIGH cloud (cirrus): stays lit ~2° longer than uTint's deck
uniform vec3  uFloorZ;   // night floor at the zenith (display sRGB, already × the night gate; 0 by day)
uniform vec3  uFloorH;   // night floor at the horizon
uniform vec3  uDogs;     // sundogs: x strength (frame param), y separation from the sun (detail units), z tail length (detail units)
uniform vec3  uCorona;   // corona: x strength (frame param), y ring radius (detail units), z purity 0–1
uniform float uAlpha;    // crossfade opacity of this pass

float hash(vec2 p){
  // precision-safe hash (Dave Hoskins): no giant intermediate products,
  // so it stays pattern-free even at large gl_FragCoord values
  vec3 p3 = fract(vec3(p.xyx) * 0.1031);
  p3 += dot(p3, p3.yzx + 33.33);
  return fract((p3.x + p3.y) * p3.z);
}
float vnoise(vec2 p){
  vec2 i = floor(p);
  vec2 f = fract(p);
  vec2 u = f * f * f * (f * (f * 6.0 - 15.0) + 10.0);   // quintic: silkier gradients
  float a = hash(i);
  float b = hash(i + vec2(1.0, 0.0));
  float c = hash(i + vec2(0.0, 1.0));
  float d = hash(i + vec2(1.0, 1.0));
  return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}
float fbm(vec2 p){
  float v = 0.0;
  float a = 0.52;
  mat2 rot = mat2(0.8, 0.6, -0.6, 0.8);
  for (int i = 0; i < 4; i++){       // fewer octaves = big soft masses, no wisps
    v += a * vnoise(p);
    p = rot * p * 1.9;
    a *= 0.5;
  }
  return v;
}
// ---- sky geometry (phenomena plan, step 0) ----
// The page's projection (CPU bodyY): the horizon line sits at SCENE y
// 0.10 and 90° of altitude spans 0.80 of the reference height, so ONE
// reference-height unit is 112.5°. Horizontal offsets are measured in
// the same units (kS.x), so the same scale applies. scatterAngle() is the angle
// between this pixel's view ray and the sun, in radians, from the sun's
// TRUE altitude (uSun.z) — the body's screen y is compressed below the
// horizon, so twilight geometry must not read it. uSun is the SUN even
// after the glow body has handed over to the moon (−5°): twilight
// geometry never jumps to the moon's azimuth. Every sun-relative sky
// layer (polarizer band, aureole, purple light) reads this one function.
float scatterAngle(vec2 uv, vec2 sunPos, float sunAltDeg, vec2 kS){
  // altitude: exact (uv.y − 0.10)·112.5° when the detail unit is the height
  // (kS.y = 1); on a narrower window the sky is measured in detail units
  // around the SUN's true altitude, so the angle stays isotropic with the
  // horizontal axis and the halo, aureole and arc keep their shapes
  float a1 = (sunAltDeg + ((uv.y - 0.10) * 112.5 - sunAltDeg) * kS.y) * 0.017453292;
  float a2 = sunAltDeg * 0.017453292;                                  // sun altitude
  float daz = (uv.x - sunPos.x) * kS.x * 112.5 * 0.017453292;          // azimuth offset
  float c = sin(a1) * sin(a2) + cos(a1) * cos(a2) * cos(daz);
  return acos(clamp(c, -1.0, 1.0));
}
// Rayleigh degree of polarization = the sky's PURITY map: 0 at the sun,
// 1 at 90° from it (L&L §2.2: bluest at 90°, flattest near the sun)
float purity(float theta){
  float s = sin(theta), c = cos(theta);
  return (s * s) / (1.0 + c * c);
}
void main(){
  vec2 uv = gl_FragCoord.xy / uRes;
  // SCENE SCALE (owner, 2026-09-22): the SKY fits the height — uv.y is the
  // sky coordinate everywhere (0.10 horizon, 0.90 zenith), so the whole
  // sky is always in frame and horizon layers ride the bottom edge. The
  // DETAILS live in uDet, the tighter of height and width/aspect: p is the
  // field frame in detail units (centered), kS converts a window-fraction
  // offset into detail units on each axis. On a landscape window uDet is
  // the height and kS = (W/H, 1): the tuned look, unchanged. On a phone
  // uDet is the width/aspect, so the sun's halo, aureole and arc shrink
  // to fit instead of running off the sides.
  vec2 p  = (gl_FragCoord.xy - 0.5 * uRes) / uDet;
  vec2 kS = uRes / uDet;
  float t = uTime;
  vec2 pw = p - uDrift * t * 0.018;   // advect the whole field downwind

  // one gentle layer of domain warping -> haze that breathes, not smoke that swirls
  vec2 q = vec2(
    fbm(pw * 1.0 + t * vec2(0.010, 0.006)),
    fbm(pw * 1.0 + vec2(5.2, 1.3) - t * vec2(0.007, 0.009))
  );
  vec2 r = vec2(
    fbm(pw * 1.4 + 1.1 * uWarp * q + vec2(1.7, 9.2) + t * vec2(0.0035, -0.0025)),
    fbm(pw * 1.4 + 1.1 * uWarp * q + vec2(8.3, 2.8) - t * vec2(0.0025, 0.0035))
  );
  float f = fbm(pw * 1.1 + 0.8 * uWarp * r);
  f = mix(f, 0.5, 0.35);             // flatten the field: diffuse, defocused

  // vertical 4-stop gradient, gently perturbed by the cloud field
  float g = clamp(uv.y + (f - 0.5) * (0.18 + 0.6 * uDensity) * (0.88 + 0.3 * uv.y), 0.0, 1.0);
  // height compression: an exponent < 1 pushes the sample point up, so
  // the lower stops' colors get squeezed into a SHORTER band — the
  // sunset glow physically sinks with the sun instead of thinning tall
  float gm = pow(g, uSquash);
  vec3 col = mix(uC3, uC2, smoothstep(0.0, 0.45, gm));
  col = mix(col, uC1, smoothstep(0.32, 0.78, gm));
  col = mix(col, uC0, smoothstep(0.62, 1.0, gm));

  // POLARIZER BAND (phenomena plan step 0/1 — L&L §2.2): skylight is
  // ~85% polarized 90° from the sun and unpolarized near it. Invisible
  // to the eye; a polarizing filter removes that component and the sky
  // deepens along a great arc 90° from the sun — the cobalt band of
  // landscape photography. The arc sweeps with the sun through the day;
  // haze depolarizes it (the CPU folds turbidity, deck and the daylight
  // gate into uPolar). Acts on the SKY between clouds: the field's bright
  // material is excluded the way uTint selects it. Darkens and pushes
  // chroma along the arc; never lightens.
  float skyM = 1.0 - smoothstep(0.47, 0.78, f);   // open sky between the field's bright material
  float th = scatterAngle(uv, uSun.xy, uSun.z, kS);   // this pixel's angle from the sun (sky + cloud lighting)
  if (uPolar + uAureole.x > 0.001){
    if (uPolar > 0.001){
      float pk = purity(th) * uPolar * skyM;
      float pl = dot(col, vec3(0.299, 0.587, 0.114));
      col = max(mix(col, (col - pl) * 1.25 + pl, pk), 0.0);   // chroma up along the arc
      col *= 1.0 - 0.45 * pk;                                 // and darker: ~0.9 stop at full
    }
    // AUREOLE (phenomena plan step 1 — L&L §2.2 Fig. 2.2A): the sky near
    // the sun is BRIGHTER and WHITER — 10° from the sun the spectrum is
    // nearly flat, 90° away it is bluest. Two lobes: the broad Rayleigh
    // rise (1+cos²θ: twice as bright at the sun as at 90°) that every
    // clear sky has, and the aerosol forward-scatter lobe whose width
    // the CPU grows with turbidity (crisp day: small hard sun on deep
    // blue; humid day: broad soft aureole on a paler sky). Flattens the
    // spectrum toward the sun's transmitted color and lifts it, screened
    // so it never clips. Sky only; the glow body stays what it is.
    if (uAureole.x > 0.001){
      float cth = cos(th);
      float lobeR = cth * cth * step(0.0, cth);                      // 1 at the sun, 0 at 90°, none behind
      float lobeM = exp(-(th * 57.29578) / max(uAureole.y, 1.0));    // forward lobe, half-width in degrees
      float au = uAureole.x * (0.35 * lobeR + 0.65 * lobeM) * skyM;
      vec3 illum = mix(vec3(1.0), uGlowCol, 0.5);
      float lumA = dot(col, vec3(0.299, 0.587, 0.114));
      col = mix(col, illum * lumA, au * 0.7);          // whiter: the spectrum flattens toward the sun
      col += au * 0.55 * illum * (1.0 - col);          // brighter: screened lift toward the illuminant
    }
  }

  // PURPLE LIGHT + CREPUSCULAR SHADOW FAN (phenomena plan step 2 —
  // L&L §2.14, Minnaert §221). Twenty to thirty minutes after sunset the
  // lower air is in the earth's shadow while dust at 20–25 km is still
  // lit: reddened direct light off that dust plus blue skylight = a
  // diffuse PURPLE glow ~40–45° above the sunset point, 10–20° across.
  // Screen-space lobe anchored to the sun's azimuth (uSun.x — the body
  // itself is below the bottom edge by now, and the glow may already be
  // the moon's), Gaussian, 1.6σ wide
  // and σ tall, none below the horizon line; a screened lift of a
  // magenta leaning toward the sun's transmitted color. The CPU owns the
  // timing (real sun −3…−8°, peak −5°) and the dust (day seed).
  // CREPUSCULAR RAYS are shadows IN this light: clouds hidden below the
  // western horizon cast dark strips that fan up from the sub-horizon
  // sun point (angular value-noise on a circle around it, godray-style,
  // no seam), most visible near their vanishing point and blue-green —
  // the shadowed sky. Structural pairing (Minnaert: no purple light, no
  // rays): the strips REMOVE purple light and cool the low sky under it.
  if (uPurple.x > 0.001){
    vec2 gpd = (uv - uSun.xy) * kS;   // from the sun point, detail units (the fan stays round)
    float altP = (uv.y - 0.10) * 112.5;   // lobe height: sky coordinate, horizon-anchored
    float dxD = gpd.x * 112.5;
    float dyD = altP - uPurple.y;
    float sigP = max(uPurple.z, 1.0);
    float lobe = exp(-0.5 * (dxD * dxD / (2.56 * sigP * sigP) + dyD * dyD / (sigP * sigP)))
               * smoothstep(-2.0, 6.0, altP);
    float shadow = 0.0;
    if (uCrep.x > 0.001){
      float drP = max(length(gpd), 1e-4);
      vec2 dirP = gpd / drP;
      float crA = 0.7 * vnoise(dirP * uCrep.y + vec2(3.1, 8.7) + t * 0.004)
                + 0.3 * vnoise(dirP * uCrep.y * 2.1 + vec2(6.4, 1.9) - t * 0.003);
      float strips = smoothstep(0.58, 0.82, crA);
      shadow = uCrep.x * strips * exp(-drP * 1.6) * skyM;
    }
    vec3 pcol = vec3(0.64, 0.25, 0.60);   // reddened dust light + blue skylight (the glow color may be the moon's by now)
    col += uPurple.x * lobe * skyM * (1.0 - shadow) * pcol * (1.0 - col);
    col = mix(col, col * vec3(0.80, 0.86, 0.94), shadow * 0.6 * (1.0 - smoothstep(5.0, 60.0, altP)));
  }

  // luminance modulation + a soft bright "sheet". Under an overcast lid
  // the color gradient goes near-uniform and displacement stops carrying
  // the clouds, so the DECK MOTTLE takes over: uDeck (how much lid the
  // CPU composed) drives strong cloud-base luminance structure — dark
  // bases, bright thin spots — the way a real deck reads.
  col *= 1.0 + (f - 0.5) * (0.05 + 0.13 * min(uDensity, 1.7) + 0.7 * uDeck) * (0.85 + 0.45 * uv.y) * uTexV;

  // low-sun cloud tint: clouds are neutral projection screens — at low
  // sun they show the TRANSMITTED, Rayleigh-reddened beam while the sky
  // between them keeps showing scattered blue. A luminance-preserving
  // chromatic multiply on the field's bright material (uTint carries
  // the sun's transmitted disk color, amount folded in on the CPU).
  col *= mix(vec3(1.0), uTint, smoothstep(0.47, 0.78, f));

  // CLOUD LIGHTING (phenomena plan — L&L §4.18–4.19): clouds are white
  // because drops scatter every color and absorb none; a cloud is dark
  // for two reasons only — SHADOW (its own bulk, when lit from behind) or
  // THINNESS (it transmits the sky behind it and loses direct light to
  // scattering). Thickness here = the field above the material threshold.
  // uLit.x THIN: thin material darkens toward a bluer sky instead of
  // adding white. uLit.y BACK-LIGHT by the scattering angle: on the sun's
  // side thick clumps get a dark core and a bright rim, thin cloud a
  // forward-scatter glare; away from the sun (front-lit) thick cloud reads
  // brilliantly white. Day only, needs cloud — the CPU gates both.
  if (uLit.x + uLit.y > 0.001){
    float thick = smoothstep(0.58, 0.80, f);
    float thin  = smoothstep(0.47, 0.60, f) * (1.0 - smoothstep(0.60, 0.74, f));
    float rim   = smoothstep(0.56, 0.66, f) * (1.0 - smoothstep(0.66, 0.78, f));
    float thd   = th * 57.29578;
    float back  = 1.0 - smoothstep(15.0, 60.0, thd);
    float front = smoothstep(70.0, 120.0, thd);
    vec3 illumC = mix(vec3(1.0), uGlowCol, 0.6);
    col = mix(col, col * vec3(0.90, 0.95, 1.04) * 0.88, uLit.x * thin);          // thin: the sky shows through
    col *= 1.0 - uLit.y * back * thick * 0.28;                                   // back-lit thick: self-shadowed core
    col += uLit.y * back * (rim * 0.35 + thin * 0.45) * illumC * (1.0 - col);    // bright rim, glare through thin cloud
    col += uLit.y * front * thick * 0.14 * illumC * (1.0 - col);                 // front-lit thick: white
  }

  // cirrus fibratus (reference: the owner's photo): LONG semi-
  // transparent strands at a gentle angle, feathered soft — rendered
  // the way the deck blends (smooth proportional mix, NO hard
  // thresholds; thresholding read as blocky altostratus). A rotated
  // anisotropic frame gives strands their sweep, a fine cross-axis
  // striation gives the silk grain, and a slow bank field varies
  // density across the sky and dissolves patches as they drift.
  if (uPuff > 0.001){
    // the sweep angle varies like a CURRENT: the deviation field is
    // cubed, so most of the sky runs straight and then takes a smooth
    // bend before straightening again — never a continuous twist
    vec2 pa = p - uDrift * (t * 0.007);
    float dv = vnoise(pa * 0.4 + vec2(19.3, 4.7)) - 0.5;
    float ang = uCirA.x + (8.0 * dv * dv * dv) * uCirA.y;
    float ca = cos(ang), sa = sin(ang);
    vec2 pr = vec2(ca * p.x + sa * p.y, -sa * p.x + ca * p.y)
            - uDrift * (t * 0.007);
    // evolve: the fiber fields crawl along the cross axis, so strands
    // continuously REFORM (the gentle dissolve the other clouds have)
    float ev = t * uCirB.y;
    // brushy turbulence: a soft warp breaks the ruled striping into
    // stroke-like filaments inside the white gradients
    float wob = vnoise(vec2(pr.x * 2.6, pr.y * 4.0) + vec2(11.1, 6.3)) - 0.5;
    float strands = vnoise(vec2(pr.x * 0.7, pr.y * 12.0 * uCirA.z + wob * 1.5) + vec2(7.7, 2.2 + ev));
    float fine    = vnoise(vec2(pr.x * 1.6, pr.y * 26.0 * uCirA.z + wob * 3.2) + vec2(3.3, 9.1 - ev * 1.7));
    float bank    = vnoise(vec2(pr.x * 0.35, pr.y * 1.5) + vec2(3.1, 8.4)
                         + t * vec2(0.008, 0.005));
    float veil = smoothstep(0.35, 0.95, strands * 0.65 + fine * 0.35)
               * smoothstep(0.25 - uCirB.x, 0.85 - uCirB.x, bank)
               * smoothstep(0.10, 0.30, uv.y);   // gone by the lower band
    // internal texture: an ISOTROPIC cellular field breaks the smooth
    // stroke into soft flocks along its length (torn-cotton interior,
    // per the owner's reference) — a soft mask, never a hard cut
    vec2 cp = pr * (6.0 * uCirB.w);
    float cell = 0.55 * vnoise(cp + wob * 0.9 + vec2(4.2, 1.9 + ev * 0.6))
               + 0.30 * vnoise(vec2(0.8 * cp.x - 0.6 * cp.y, 0.6 * cp.x + 0.8 * cp.y) * 2.1
                               + vec2(9.4, 3.3 - ev))
               + 0.15 * vnoise(cp * 3.7 + vec2(1.3, 7.9));
    veil *= (1.0 - uCirB.z) + uCirB.z * smoothstep(0.32, 0.75, cell);
    // OVERLAY-style blend: pops on a bright sky, melts into a dark one —
    // one opacity, and the day/night behavior falls out of the math
    vec3 cw = min(uMilk * 1.22 * uTintHi, vec3(1.0));   // high cloud wears its own low-sun tint (stays lit after the deck)
    vec3 ov = mix(2.0 * col * cw,
                  1.0 - 2.0 * (1.0 - col) * (1.0 - cw),
                  step(vec3(0.5), col));
    col = mix(col, ov, uPuff * veil * uCirA.w);
    // the overlay melts into a dark base by design — at night a dim
    // ADDITIVE lift (moon/skyglow-lit fibers) keeps the strands present
    col += uMilk * veil * (uPuff * uNight * 0.9);   // night cirrus as present as the night deck clouds
  }

  // night clouds: on a dark sky the multiplicative mottle has nothing to
  // multiply — visible night clouds are LIT (moon, skyglow), i.e. an
  // ADDITIVE dim veil where the field peaks. uNight gates this to night
  // (CPU-driven, V2 only), and the ambient-aware milk keeps it navy-dim.
  if (uNight > 0.001){
    col += uMilk * clamp(f - 0.46, 0.0, 0.40) * uNight * (0.8 + 0.4 * uv.y);
  }

  // HORIZON WHITENING (phenomena plan step 1 — L&L §2.3): along a long
  // path scattered light SATURATES, I = 1 − e^−τ, and multiple scattering
  // mixes every wavelength, so the horizon sky takes the color of the
  // light feeding it — white by day, the sun's color when it is low. τ is
  // the AEROSOL depth per air mass (the Rayleigh part is already in the
  // LUT's horizon stop) times the Kasten–Young air mass of this pixel's
  // altitude (~38 at the horizon, 1 overhead), so the whitening climbs
  // exactly as far as the haze does: NOTHING on a pristine day (the
  // owner's de-murk rule), a white band climbing high on a humid or
  // smoky one. Sits in front of the clouds like the veil it is; the
  // texture-only haze below still mottles it.
  if (uWhiten > 0.001){
    // soft horizon (owner: "a tad softer where it ends"): a smooth max
    // instead of a hard clamp at the horizon line (no crease), and the
    // air-mass curve read 1° higher, so the steep first degrees relax
    // into a longer shoulder. The CPU's τ is scaled so the strength AT
    // the horizon line is unchanged for a given dial.
    float altR = (uv.y - 0.10) * 112.5;
    float altW = 0.5 * (altR + sqrt(altR * altR + 4.0)) + 1.0;
    float amW = 1.0 / (sin(altW * 0.017453292) + 0.15 * pow(altW + 3.885, -1.253));
    float wh = 1.0 - exp(-uWhiten * amW);
    float lowSunW = 1.0 - smoothstep(5.0, 25.0, uSun.z);
    vec3 illumW = mix(vec3(1.0), uGlowCol, 0.6 * lowSunW);
    // §2.6 airlight: blue skylight scattered INTO the low-sun path sits over
    // the reddened light — the band leans violet ("purple mountain")
    illumW = mix(illumW, illumW * vec3(0.88, 0.93, 1.14), uLit.z);
    float lumW = dot(col, vec3(0.299, 0.587, 0.114));
    col = mix(col, illumW * mix(lumW, 0.92, 0.6), wh);
  }

  // horizon veil, TEXTURE-ONLY (owner de-murk fix): zero-mean patchy
  // modulation of the low band — soft cloudy structure that drifts
  // with the field, with NO net whitening wash (the old mix-toward-milk
  // drained the horizon's blue into warm gray; the physical LUT already
  // carries atmospheric perspective's real color). Multiplicative, so
  // chroma is untouched by construction.
  if (uHaze > 0.001){
    float hband = 1.0 - smoothstep(0.02, 0.52, uv.y);
    float htex = smoothstep(0.30, 0.72, f) - 0.5;   // zero-mean patches
    col *= 1.0 + htex * uHaze * hband * 0.9;
  }
  float sheet = smoothstep(0.45, 0.95, r.y) * uDensity;
  col = mix(col, col * 1.05 + 0.015, sheet * 0.22);

  // clumpy fog: distinct banks with ragged interiors; cores go thick,
  // gaps open to nearly clear sky — never a flat wash
  if (uFog > 0.001){
    float fogB = fbm(pw * 0.85 + vec2(4.1, 7.7));
    float bank = smoothstep(0.42, 0.60, 0.5 * f + 0.5 * fogB);
    float tatter = smoothstep(0.35, 0.75, fbm(pw * 2.3 + vec2(9.3, 1.2)));
    float fogd = bank * (0.55 + 0.45 * tatter);
    col = mix(col, uMilk, uFog * (0.10 + 0.90 * fogd));
  }

  // sun / moon glow: a DELIBERATE compact body — a bright core with a
  // tight bloom (the old half-screen halo diffused the whole sky).
  // uGlowSize breathes it larger near the horizon, tight when high.
  vec2 gp = (uv - uGlowPos) * kS;   // detail units: the body and its halo stay round and sized
  float d2 = dot(gp, gp) / (uGlowSize * uGlowSize);
  vec3 gt = uGlowCol * uGlow * (0.60 * exp(-d2 * 55.0) + 0.25 * exp(-d2 * 11.0));
  col += gt * (1.0 - col);   // screen blend: blooms on dark skies, melts into bright ones

  // 22° solar halo: ice-crystal refraction as a delicate, strictly
  // ADDITIVE ring — sharp inner edge, long soft outer falloff, and the
  // dispersion spread as per-channel radii (warm inner rim → neutral →
  // cool outer smear; a tint gradient, never a rainbow). The distance
  // is the RAW offset — a halo's angular radius must NOT breathe with
  // the body's uGlowSize. Tinted by uGlowCol, so low-sun halos go
  // ember for free. One bank-style noise sample keeps it patchy like
  // the ice haze it lives in. moon halo: later (skyGlow reports isMoon).
  if (uHalo > 0.001){
    // slight perspective shift: on a flat projection a sun-centered
    // circle reads as displaced toward the view center — nudge the ring's
    // center from the body toward screen middle (same lens-geometry
    // family as the shear below)
    vec2 hoff = (vec2(0.5) - uGlowPos) * 0.06;
    hoff *= kS;
    vec2 hgp = gp + hoff;
    float hd = length(hgp);
    float pat = 0.6 + 0.4 * vnoise(pw * 0.9 + vec2(12.7, 3.9));
    float hw = 0.070 * uHaloLook.z;   // z: outer-falloff softness (1 = designed width)
    // slight SHEAR (owner): the ring leans with the sun's screen height —
    // a low sun stretches the top lobe wider, a high sun the bottom one
    float dirY = hgp.y / max(hd, 1e-4);
    float R = uHaloLook.x * (1.0 + (0.5 - uGlowPos.y) * 0.15 * dirY);
    // envelope: sharp inner edge; the outer falloff is stretched so the
    // geometrically-widening bands have room to finish
    float env = smoothstep(R - 0.018, R, hd) * (1.0 - smoothstep(R, R + hw * 1.45, hd));
    // IRIDESCENCE (owner: "bubble veil"): thin-film hue cycling across
    // the ring's width — per-channel cosine phases 120° apart, swirled
    // by the same patch field so the bands shimmer like soap film.
    // GEOMETRIC band growth: the innermost color keeps its designed
    // width and each successive band runs ~10% longer than the one
    // inside it (log-warped phase; 0.286 = 3·ln 1.1 for 3 bands/cycle).
    // cos ∈ [−1,1] → irid ∈ [0,1]: strictly additive, never darkens.
    // xr clamped ≥ 0: the sharp inner ramp stays pure band-0 RED (the
    // physical order — red inner rim, blue outer); the swirl is capped
    // at ±0.12 cycles so hues shimmer but can never REORDER
    float xr = max((hd - R) / hw, 0.0);
    float ph = log(1.0 + 0.286 * (0.4 * uHaloLook.y) * xr) / 0.286
             + (pat - 0.8) * 0.6;
    vec3 irid = 0.5 + 0.5 * cos(6.2831 * ph + vec3(0.0, -2.094, -4.188));
    // pure bands (no white dilution), and only a soft pull toward the
    // sun's transmitted color so the film keeps its own hues at dusk
    vec3 tintG = mix(vec3(1.0), uGlowCol, 0.4);
    col += env * irid * tintG * (uHalo * 0.13) * pat * (1.0 - col);
  }

  // SUNDOGS (phenomena plan — L&L §5.8): plate crystals falling flat
  // confine the 22° refraction to the sun's altitude, so instead of a
  // faint ring the light piles into two bright patches beside the sun.
  // The look (owner, from his photo): NOT a spot with a core — an
  // iridescent, smudged, DIRECTIONAL glare: red at the sunward edge
  // running through orange and yellow into white and a pale blue tail
  // that smears away from the sun, and broken into soft filaments by the
  // ice fibers it lives in. The CPU places them (separation from the
  // plate-crystal formula, in the halo's own angular scale so they sit
  // ON the ring at a low sun) and fades them with altitude.
  if (uDogs.x > 0.001){
    float au = uHaloLook.x / 22.0;                                     // detail units per degree
    float patD = 0.6 + 0.4 * vnoise(pw * 0.9 + vec2(12.7, 3.9));
    vec2 hoffD = (vec2(0.5) - uGlowPos) * 0.06 * kS;                   // the ring's perspective nudge
    float sIn = 1.6 * au, sOut = max(uDogs.z, 1.5 * au), sy = 2.4 * au;
    vec3 dogs = vec3(0.0);
    for (int i = 0; i < 2; i++){
      float sgn = i == 0 ? -1.0 : 1.0;
      vec2 dp = gp + hoffD - vec2(sgn * uDogs.y, 0.0);
      float ex = sgn * dp.x;                                           // outward from the sun
      float sxv = ex < 0.0 ? sIn : sOut;                               // short toward the sun, long smear away
      float body = exp(-0.5 * ex * ex / (sxv * sxv)) * exp(-0.5 * dp.y * dp.y / (sy * sy));
      float tq = clamp((ex + sIn) / (sIn + 1.6 * sOut), 0.0, 1.0);     // position along the smear
      vec3 spec = tq < 0.25 ? mix(vec3(1.0, 0.35, 0.22), vec3(1.0, 0.62, 0.25), tq / 0.25)
                : tq < 0.5  ? mix(vec3(1.0, 0.62, 0.25), vec3(1.0, 0.92, 0.55), (tq - 0.25) / 0.25)
                : tq < 0.75 ? mix(vec3(1.0, 0.92, 0.55), vec3(1.0, 1.0, 0.98), (tq - 0.5) / 0.25)
                :             mix(vec3(1.0, 1.0, 0.98), vec3(0.82, 0.90, 1.0), (tq - 0.75) / 0.25);
      dogs += body * spec;
    }
    // smudged into the fibers: a horizontally stretched streak field
    // breaks the glare into soft filaments, drifting with the field
    float smear = 0.55 + 0.45 * vnoise(vec2(p.x * 2.2, p.y * 7.0) + vec2(3.3, 8.1) + t * vec2(0.010, 0.004));
    vec3 tintD = mix(vec3(1.0), uGlowCol, 0.5);
    col += dogs * tintD * (uDogs.x * 0.7) * patD * smear * (1.0 - col);
  }

  // CORONA (phenomena plan — L&L §4.13): diffraction around the drops of
  // a thin cloud in front of the body — a bluish-white aureole a degree
  // or two across and a pale rust-brown ring a few degrees out, red on
  // the outside; uniform drops give a purer, narrower ring with a faint
  // greenish band inside it. Lives where the thin cloud is (weighted by
  // the field), around the sun or the moon alike; the CPU gates it to
  // thin mid cloud and favors the moon.
  if (uCorona.x > 0.001){
    float dC = length(gp);
    float auC = uHaloLook.x / 22.0;
    float aur = exp(-0.5 * dC * dC / (1.4 * auC * 1.4 * auC));
    float rw = 0.9 * auC * mix(1.6, 0.8, uCorona.z);
    float ring = exp(-0.5 * pow((dC - uCorona.y) / rw, 2.0));
    float inner = exp(-0.5 * pow((dC - uCorona.y * 0.78) / rw, 2.0)) * uCorona.z;
    vec3 ringC = mix(vec3(0.85, 0.72, 0.60), vec3(0.95, 0.55, 0.35), uCorona.z);
    vec3 cC = aur * vec3(0.80, 0.86, 1.0) * 0.55
            + ring * ringC * (0.25 + 0.35 * uCorona.z)
            + inner * vec3(0.55, 0.80, 0.75) * 0.15;
    float cm = 0.5 + 0.5 * smoothstep(0.45, 0.70, f);                  // brightest where the thin cloud is
    col += uCorona.x * cC * cm * (1.0 - col);
  }

  // sun godrays (abstract, Minecraft-spirited): broad soft beams fan
  // out from the glow — angular value-noise sampled ON A CIRCLE (no
  // atan seam), two octaves for irregular beam widths, slowly rotating
  // and morphing. They wear the glow's own color and screen onto the
  // sky, so ember sunsets throw ember rays.
  if (uRays > 0.001){
    float dr = max(sqrt(d2), 1e-4);
    float rc = cos(t * 0.008), rs = sin(t * 0.008);       // slow fan rotation
    vec2 dirn = vec2(rc * gp.x - rs * gp.y, rs * gp.x + rc * gp.y) / dr;
    float raysA = 0.75 * vnoise(dirn * uRayLook.x + vec2(7.3, 2.1) + t * 0.012)
                + 0.25 * vnoise(dirn * uRayLook.x * 2.3 + vec2(1.7, 9.4) - t * 0.009);
    float beam = smoothstep(0.30, 0.95, raysA);
    // beams RESOLVE with distance: angular spokes all converge at the
    // body, joining into a busy knot — near it, melt the fan into its
    // smooth average and let the beams separate further out (scaled by
    // the body's breathing size so low suns keep a clean core too)
    beam = mix(0.45, beam, smoothstep(0.05 * uGlowSize, 0.22 * uGlowSize, dr));
    float k = uRays * beam * exp(-dr * uRayLook.y);
    col += uGlowCol * k * (1.0 - col);
  }

  // distant rain shafts: soft precipitation curtains hanging from the cloud
  // base at the top of the frame, dissolving as they fall. Three motions,
  // all whisper-slow: lateral drift with the wind, edge billow, and an
  // internal downward creep inside each curtain.
  if (uPrecip.x + uPrecip.y > 0.001){
    float fall = t * uPrecip.z;
    float drop = 1.0 - uv.y;                                  // 0 at cloud base
    float slant = uDrift.x * 0.22;
    float xs = p.x - drop * slant - fall * 0.010 * uDrift.x;

    // curtain envelope: coverage knob widens the columns
    float wob = (vnoise(vec2(xs * 2.2, drop * 1.4 + t * 0.02)) - 0.5) * 0.35;
    float env = vnoise(vec2((xs + wob) * 2.6, 3.7 + t * 0.006));
    float lo = 0.55 - uShaft.y * 0.28;
    float shaft = smoothstep(lo, lo + 0.32, env);

    // internal striation creeping downward
    float stria = vnoise(vec2(xs * 13.0, (uv.y + fall * 0.06) * 3.0));
    shaft *= 0.65 + 0.35 * smoothstep(0.25, 0.85, stria);

    // attached to the top; reach knob sets how far down before dissolving
    float fl = 0.75 - uShaft.z * 0.75;
    float vfade = smoothstep(fl, fl + 0.60, uv.y);

    // the curtain's contrast ADAPTS to the sky behind it: against a dark
    // rainy sky it's a touch brighter (the original tuning); against the
    // v2 bright deck it goes DARKER — real rain curtains read as dark
    // veils on a bright lid. Fixes shafts vanishing bright-on-bright.
    vec3 mid = mix(uC1, uC2, 0.5);
    float midL = dot(mid, vec3(0.299, 0.587, 0.114));
    float brightSky = smoothstep(0.50, 0.62, midL);
    vec3 mist = mix(mid, vec3(midL), 0.5) * mix(1.10, 0.76, brightSky)
              + vec3(0.04 * (1.0 - brightSky));
    vec3 mistR = min(mist * vec3(0.92, 0.96, 1.03), vec3(1.0));  // rain: cool gray
    vec3 mistS = min(mix(mist, vec3(0.94, 0.95, 0.97), 0.55), vec3(1.0));
    col = mix(col, mistR, clamp(shaft * vfade * 0.55 * uPrecip.x * uShaft.x, 0.0, 1.0));
    col = mix(col, mistS, clamp(shaft * vfade * 0.60 * uPrecip.y * uShaft.x, 0.0, 1.0));
  }

  // whisper of a vignette
  float vd = length((uv - 0.5) * vec2(uRes.x / uRes.y, 1.0));
  col *= 1.0 - 0.20 * smoothstep(0.6, 1.3, vd);

  // owner color LUT: the Lightroom grade, applied to the finished frame
  // BEFORE the grain/dither (grain rides on top; dither still fights
  // any banding the grade introduces). 33³ strip, two slice taps + mix.
  if (uLutAmt > 0.001){
    vec3 lc = clamp(col, 0.0, 1.0);
    float b33 = lc.b * 32.0;
    float sl0 = floor(b33);
    float sl1 = min(sl0 + 1.0, 32.0);
    vec2 luv0 = vec2((sl0 * 33.0 + lc.r * 32.0 + 0.5) / 1089.0, (lc.g * 32.0 + 0.5) / 33.0);
    vec2 luv1 = vec2((sl1 * 33.0 + lc.r * 32.0 + 0.5) / 1089.0, (lc.g * 32.0 + 0.5) / 33.0);
    vec3 graded = mix(texture2D(uLut, luv0).rgb, texture2D(uLut, luv1).rgb, b33 - sl0);
    col = mix(col, graded, uLutAmt);
  }

  // NIGHT FLOOR (owner, 2026-09-22): the deepest night never falls to
  // black. A screen-lift toward a very dark blue, zenith→horizon, applied
  // AFTER the vignette and the LUT (which push below the grade's own
  // linear floor) and before the grain. The CPU gates it to night, so by
  // day both colors are zero and this is a no-op.
  vec3 nf = mix(uFloorH, uFloorZ, clamp(uv.y, 0.0, 1.0));
  col = nf + col * (1.0 - nf);

  // static photographic grain: three decorrelated taps averaged toward a
  // gaussian, like Photoshop's monochromatic noise (x1.7 restores strength)
  float gn = (hash(gl_FragCoord.xy)
            + hash(gl_FragCoord.xy + 17.13)
            + hash(gl_FragCoord.xy + 41.77)) / 3.0;
  col += (gn - 0.5) * uGrain * 1.7;
  col += (hash(gl_FragCoord.xy * 1.7 + 31.0) - 0.5) * (1.6 / 255.0);

  gl_FragColor = vec4(col, uAlpha);
}

