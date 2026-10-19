window.COMPARISON_REPORT = {
  "schemaVersion": 1,
  "generatedAtUtc": "2026-10-15T13:16:52.276819+00:00",
  "comparisonRoiPx": [
    0,
    96,
    1080,
    2334
  ],
  "roiConvention": "left/top inclusive, right/bottom exclusive; fixed crop excludes y<96 and y>=2334; actual reference AOSP decor begins at136, so y96..136 can still include reference system-bar pixels",
  "conditions": {
    "canvasPx": [
      1080,
      2400
    ],
    "density": 2.75,
    "densityDpi": 440,
    "locale": "zh-CN",
    "theme": "day",
    "fontScale": 1,
    "conditionSource": "task baseline; dimensions independently checked; other settings require capture/environment records"
  },
  "limitations": [
    "No similarity score is produced.",
    "Ink boxes are not OCR or font baselines.",
    "Geometric candidate pairing is not verified semantic correspondence.",
    "Exact RGB regions identify flat-color pixels, not full rounded-control geometry.",
    "Static screenshots cannot verify motion, gestures, state retention or async behavior.",
    "REA screenshot records alone do not bind the foreground window to the APK; use the runtime identity evidence alongside them."
  ],
  "pages": [
    {
      "id": "health-day",
      "label": "健康概览",
      "sources": {
        "original": {
          "path": "../runtime/health-day/screen.png",
          "sha256": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        },
        "reference": {
          "path": "../reference/health-day/screen.png",
          "sha256": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        }
      },
      "originalEvidence": {
        "evidenceId": "ev_716b470d4284b551bafa1e3ddb5f5493f9a6e535fb6c1065b73866e2777c7963",
        "record": "../runtime/healthcap.rea.json",
        "expectedSha256": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "referenceEvidence": {
        "evidenceId": "ev_f55fe72599012450e78da26423dd5fea6f6392270cc325a9440c8729161265e6",
        "record": "../reference/health-day.rea.json",
        "expectedSha256": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "images": {
        "sideBySide": {
          "path": "generated/health-day-side-by-side.png",
          "sha256": "972e529227218debfc9b34b105942c2e7619d8caecb3d27e2c072a374ec464d7",
          "canvasPx": [
            2160,
            2400
          ]
        },
        "overlay": {
          "path": "generated/health-day-overlay.png",
          "sha256": "f7183f834efcb6a73bf9d25cacfd4514c75d19c8b70894d867026eaf3e5ed17c",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "difference": {
          "path": "generated/health-day-difference-x3.png",
          "sha256": "d7e0486c53d41dc90d9e3ff67f8c7addc9c4125209822e039657cfda3edb5b6c",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "rawDifference": {
          "path": "generated/health-day-difference-raw.png",
          "sha256": "0630d36ffc7f45726a5a0f178be19fdb7d19ce17918b7225c4776768fa8c4e1b",
          "canvasPx": [
            1080,
            2400
          ]
        }
      },
      "overlayBlendWeight": 0.5,
      "differenceDisplayAmplification": 3,
      "knownDifferences": [
        {
          "component": "ProgressHero",
          "classification": "D versus A",
          "observation": "D三环下端平切，A有局部圆滑收口；D倒影资产/渐变为近似，未复现原版算法。",
          "impact": "本页最显著的视觉差异；非零进度和动效仍未知。"
        },
        {
          "component": "ProgressHero.share",
          "classification": "D versus A",
          "observation": "饼干/计数偏右下；x0字形A[943,714,990,746]→D[950,728,996,759]。A分享面白描边/阴影，D普通圆角面缺少这一层。"
        },
        {
          "component": "TopBar",
          "classification": "D versus A",
          "observation": "添加图标墨迹A[943,183,1008,248]→D[943,176,1008,241]，D上移7px。y96..136处A白、D#F6F7F9；这是参考AOSP系统栏占位差异落入固定ROI，单独解释。"
        },
        {
          "component": "ProgressHero.summary",
          "classification": "D versus A",
          "observation": "卡路里字形A[119,866,232,903]→D[124,866,234,903]；步数A[466,866,540,903]→D[454,866,527,903]。列内图文箭头间距、目标行字重/行位仍有差。活动次数正文右边界A384、D365。"
        },
        {
          "component": "MetricCard/ProfileHeader variant",
          "classification": "D versus A",
          "observation": "未登录标题A[203,1426,374,1468]→D[203,1422,383,1467]；睡眠标题A[78,1764,168,1808]→D[77,1762,165,1806]，字形宽度/字重与位置仍有差；D箭头更细小。"
        },
        {
          "component": "BottomNav",
          "classification": "D versus A",
          "observation": "白面起点A2151、D2152。健康选中图标橙色框A[105,2189,165,2249]→D[103,2187,168,2252]，D稍大；文字位置/字重可见轻微重影。"
        }
      ],
      "matchedDetails": [
        "健康标题深色字形包围框A/D均[75,179,244,262]；轻标题角色和位置在本模拟器默认字体下对应，不能推断小米真机逐字字体相同。",
        "三摘要面A[33,797,1047,1319]→D[33,797,1047,1320]；左右和顶部对应、底部差1px。未登录面A/D均[33,1358,1047,1592]。",
        "三指标为0、未登录及睡眠/心率空态的内容顺序对应；不涵盖非零数据。"
      ],
      "unknowns": [
        "Hero非零进度、实时倒影算法、动画时间与中断未知。",
        "空指标卡被固定导航截断，完整高度不能从本图确定。",
        "OEM及逐字中文fallback face未知。",
        "D二级边界说明和D认证弹层并非原版详情/账号SDK视觉复刻。"
      ],
      "manualReviewStatus": "reviewed-with-differences",
      "regions": {
        "original": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    511
                  ],
                  "pixelArea": 370849,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    185.818
                  ]
                },
                {
                  "bboxPx": [
                    91,
                    187,
                    109,
                    257
                  ],
                  "pixelArea": 615,
                  "bboxDp": [
                    33.091,
                    68.0,
                    39.636,
                    93.455
                  ]
                },
                {
                  "bboxPx": [
                    948,
                    188,
                    1003,
                    243
                  ],
                  "pixelArea": 2008,
                  "bboxDp": [
                    344.727,
                    68.364,
                    364.727,
                    88.364
                  ]
                },
                {
                  "bboxPx": [
                    136,
                    193,
                    144,
                    199
                  ],
                  "pixelArea": 48,
                  "bboxDp": [
                    49.455,
                    70.182,
                    52.364,
                    72.364
                  ]
                },
                {
                  "bboxPx": [
                    136,
                    205,
                    144,
                    211
                  ],
                  "pixelArea": 48,
                  "bboxDp": [
                    49.455,
                    74.545,
                    52.364,
                    76.727
                  ]
                },
                {
                  "bboxPx": [
                    213,
                    206,
                    229,
                    212
                  ],
                  "pixelArea": 96,
                  "bboxDp": [
                    77.455,
                    74.909,
                    83.273,
                    77.091
                  ]
                },
                {
                  "bboxPx": [
                    213,
                    218,
                    229,
                    224
                  ],
                  "pixelArea": 96,
                  "bboxDp": [
                    77.455,
                    79.273,
                    83.273,
                    81.455
                  ]
                },
                {
                  "bboxPx": [
                    187,
                    230,
                    205,
                    242
                  ],
                  "pixelArea": 120,
                  "bboxDp": [
                    68.0,
                    83.636,
                    74.545,
                    88.0
                  ]
                },
                {
                  "bboxPx": [
                    213,
                    230,
                    234,
                    240
                  ],
                  "pixelArea": 114,
                  "bboxDp": [
                    77.455,
                    83.636,
                    85.091,
                    87.273
                  ]
                },
                {
                  "bboxPx": [
                    369,
                    412,
                    711,
                    511
                  ],
                  "pixelArea": 4255,
                  "bboxDp": [
                    134.182,
                    149.818,
                    258.545,
                    185.818
                  ]
                },
                {
                  "bboxPx": [
                    492,
                    499,
                    588,
                    511
                  ],
                  "pixelArea": 759,
                  "bboxDp": [
                    178.909,
                    181.455,
                    213.818,
                    185.818
                  ]
                },
                {
                  "bboxPx": [
                    887,
                    577,
                    1047,
                    764
                  ],
                  "pixelArea": 1052,
                  "bboxDp": [
                    322.545,
                    209.818,
                    380.727,
                    277.818
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    797,
                    1047,
                    1319
                  ],
                  "pixelArea": 498621,
                  "bboxDp": [
                    12.0,
                    289.818,
                    380.727,
                    479.636
                  ]
                },
                {
                  "bboxPx": [
                    756,
                    872,
                    764,
                    889
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    274.909,
                    317.091,
                    277.818,
                    323.273
                  ]
                },
                {
                  "bboxPx": [
                    791,
                    877,
                    800,
                    886
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    287.636,
                    318.909,
                    290.909,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    805,
                    877,
                    814,
                    886
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    292.727,
                    318.909,
                    296.0,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    169,
                    879,
                    180,
                    886
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    61.455,
                    319.636,
                    65.455,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    519,
                    884,
                    529,
                    899
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    188.727,
                    321.455,
                    192.364,
                    326.909
                  ]
                },
                {
                  "bboxPx": [
                    179,
                    892,
                    187,
                    898
                  ],
                  "pixelArea": 48,
                  "bboxDp": [
                    65.091,
                    324.364,
                    68.0,
                    326.545
                  ]
                },
                {
                  "bboxPx": [
                    86,
                    957,
                    102,
                    991
                  ],
                  "pixelArea": 449,
                  "bboxDp": [
                    31.273,
                    348.0,
                    37.091,
                    360.364
                  ]
                },
                {
                  "bboxPx": [
                    416,
                    957,
                    432,
                    991
                  ],
                  "pixelArea": 449,
                  "bboxDp": [
                    151.273,
                    348.0,
                    157.091,
                    360.364
                  ]
                },
                {
                  "bboxPx": [
                    746,
                    957,
                    762,
                    991
                  ],
                  "pixelArea": 449,
                  "bboxDp": [
                    271.273,
                    348.0,
                    277.091,
                    360.364
                  ]
                },
                {
                  "bboxPx": [
                    315,
                    1217,
                    326,
                    1239
                  ],
                  "pixelArea": 190,
                  "bboxDp": [
                    114.545,
                    442.545,
                    118.545,
                    450.545
                  ]
                },
                {
                  "bboxPx": [
                    186,
                    1223,
                    201,
                    1239
                  ],
                  "pixelArea": 112,
                  "bboxDp": [
                    67.636,
                    444.727,
                    73.091,
                    450.545
                  ]
                },
                {
                  "bboxPx": [
                    202,
                    1223,
                    210,
                    1242
                  ],
                  "pixelArea": 96,
                  "bboxDp": [
                    73.455,
                    444.727,
                    76.364,
                    451.636
                  ]
                },
                {
                  "bboxPx": [
                    271,
                    1228,
                    281,
                    1243
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    98.545,
                    446.545,
                    102.182,
                    452.0
                  ]
                },
                {
                  "bboxPx": [
                    157,
                    1235,
                    169,
                    1241
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    57.091,
                    449.091,
                    61.455,
                    451.273
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1358,
                    1047,
                    1592
                  ],
                  "pixelArea": 215199,
                  "bboxDp": [
                    12.0,
                    493.818,
                    380.727,
                    578.909
                  ]
                },
                {
                  "bboxPx": [
                    120,
                    1435,
                    139,
                    1453
                  ],
                  "pixelArea": 263,
                  "bboxDp": [
                    43.636,
                    521.818,
                    50.545,
                    528.364
                  ]
                },
                {
                  "bboxPx": [
                    111,
                    1460,
                    148,
                    1474
                  ],
                  "pixelArea": 470,
                  "bboxDp": [
                    40.364,
                    530.909,
                    53.818,
                    536.0
                  ]
                },
                {
                  "bboxPx": [
                    670,
                    1500,
                    680,
                    1506
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    243.636,
                    545.455,
                    247.273,
                    547.636
                  ]
                },
                {
                  "bboxPx": [
                    209,
                    1502,
                    218,
                    1517
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    76.0,
                    546.182,
                    79.273,
                    551.636
                  ]
                },
                {
                  "bboxPx": [
                    604,
                    1505,
                    618,
                    1519
                  ],
                  "pixelArea": 87,
                  "bboxDp": [
                    219.636,
                    547.273,
                    224.727,
                    552.364
                  ]
                },
                {
                  "bboxPx": [
                    580,
                    1507,
                    589,
                    1515
                  ],
                  "pixelArea": 36,
                  "bboxDp": [
                    210.909,
                    548.0,
                    214.182,
                    550.909
                  ]
                },
                {
                  "bboxPx": [
                    669,
                    1509,
                    680,
                    1520
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    243.273,
                    548.727,
                    247.273,
                    552.727
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1620,
                    540,
                    2150
                  ],
                  "pixelArea": 207038,
                  "bboxDp": [
                    12.0,
                    589.091,
                    196.364,
                    781.818
                  ]
                },
                {
                  "bboxPx": [
                    568,
                    1620,
                    1047,
                    2150
                  ],
                  "pixelArea": 225289,
                  "bboxDp": [
                    206.545,
                    589.091,
                    380.727,
                    781.818
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    1692,
                    127,
                    1703
                  ],
                  "pixelArea": 198,
                  "bboxDp": [
                    34.909,
                    615.273,
                    46.182,
                    619.273
                  ]
                },
                {
                  "bboxPx": [
                    636,
                    1698,
                    657,
                    1710
                  ],
                  "pixelArea": 149,
                  "bboxDp": [
                    231.273,
                    617.455,
                    238.909,
                    621.818
                  ]
                },
                {
                  "bboxPx": [
                    290,
                    1835,
                    302,
                    1840
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    105.455,
                    667.273,
                    109.818,
                    669.091
                  ]
                },
                {
                  "bboxPx": [
                    325,
                    1847,
                    335,
                    1863
                  ],
                  "pixelArea": 80,
                  "bboxDp": [
                    118.182,
                    671.636,
                    121.818,
                    677.455
                  ]
                },
                {
                  "bboxPx": [
                    135,
                    1848,
                    145,
                    1854
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    49.091,
                    672.0,
                    52.727,
                    674.182
                  ]
                },
                {
                  "bboxPx": [
                    860,
                    1850,
                    870,
                    1866
                  ],
                  "pixelArea": 80,
                  "bboxDp": [
                    312.727,
                    672.727,
                    316.364,
                    678.545
                  ]
                },
                {
                  "bboxPx": [
                    670,
                    1851,
                    680,
                    1857
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    243.636,
                    673.091,
                    247.273,
                    675.273
                  ]
                },
                {
                  "bboxPx": [
                    370,
                    1857,
                    381,
                    1862
                  ],
                  "pixelArea": 55,
                  "bboxDp": [
                    134.545,
                    675.273,
                    138.545,
                    677.091
                  ]
                },
                {
                  "bboxPx": [
                    905,
                    1860,
                    916,
                    1865
                  ],
                  "pixelArea": 55,
                  "bboxDp": [
                    329.091,
                    676.364,
                    333.091,
                    678.182
                  ]
                },
                {
                  "bboxPx": [
                    0,
                    2151,
                    1080,
                    2334
                  ],
                  "pixelArea": 181933,
                  "bboxDp": [
                    0.0,
                    782.182,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    120,
                    2204,
                    150,
                    2234
                  ],
                  "pixelArea": 652,
                  "bboxDp": [
                    43.636,
                    801.455,
                    54.545,
                    812.364
                  ]
                },
                {
                  "bboxPx": [
                    674,
                    2207,
                    687,
                    2220
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    245.091,
                    802.545,
                    249.818,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    413,
                    2281,
                    423,
                    2291
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    150.182,
                    829.455,
                    153.818,
                    833.091
                  ]
                }
              ],
              "totalQualifyingRegions": 50
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    1075,
                    1080,
                    2150
                  ],
                  "pixelArea": 157068,
                  "bboxDp": [
                    0.0,
                    390.909,
                    392.727,
                    781.818
                  ]
                },
                {
                  "bboxPx": [
                    75,
                    2000,
                    498,
                    2110
                  ],
                  "pixelArea": 46394,
                  "bboxDp": [
                    27.273,
                    727.273,
                    181.091,
                    767.273
                  ]
                }
              ],
              "totalQualifyingRegions": 2
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    105,
                    2189,
                    165,
                    2249
                  ],
                  "pixelArea": 1824,
                  "bboxDp": [
                    38.182,
                    796.0,
                    60.0,
                    817.818
                  ]
                },
                {
                  "bboxPx": [
                    121,
                    2270,
                    134,
                    2294
                  ],
                  "pixelArea": 142,
                  "bboxDp": [
                    44.0,
                    825.455,
                    48.727,
                    834.182
                  ]
                },
                {
                  "bboxPx": [
                    137,
                    2270,
                    164,
                    2298
                  ],
                  "pixelArea": 361,
                  "bboxDp": [
                    49.818,
                    825.455,
                    59.636,
                    835.636
                  ]
                },
                {
                  "bboxPx": [
                    113,
                    2272,
                    133,
                    2297
                  ],
                  "pixelArea": 108,
                  "bboxDp": [
                    41.091,
                    826.182,
                    48.364,
                    835.273
                  ]
                }
              ],
              "totalQualifyingRegions": 4
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    274,
                    868,
                    290,
                    897
                  ],
                  "pixelArea": 160,
                  "bboxDp": [
                    99.636,
                    315.636,
                    105.455,
                    326.182
                  ]
                },
                {
                  "bboxPx": [
                    582,
                    868,
                    598,
                    897
                  ],
                  "pixelArea": 160,
                  "bboxDp": [
                    211.636,
                    315.636,
                    217.455,
                    326.182
                  ]
                },
                {
                  "bboxPx": [
                    979,
                    868,
                    995,
                    897
                  ],
                  "pixelArea": 160,
                  "bboxDp": [
                    356.0,
                    315.636,
                    361.818,
                    326.182
                  ]
                },
                {
                  "bboxPx": [
                    962,
                    1212,
                    978,
                    1239
                  ],
                  "pixelArea": 126,
                  "bboxDp": [
                    349.818,
                    440.727,
                    355.636,
                    450.545
                  ]
                },
                {
                  "bboxPx": [
                    944,
                    1460,
                    960,
                    1489
                  ],
                  "pixelArea": 160,
                  "bboxDp": [
                    343.273,
                    530.909,
                    349.091,
                    541.455
                  ]
                }
              ],
              "totalQualifyingRegions": 5
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                75,
                179,
                244,
                262
              ],
              "bboxDp": [
                27.273,
                65.091,
                88.727,
                95.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                943,
                183,
                1008,
                248
              ],
              "bboxDp": [
                342.909,
                66.545,
                366.545,
                90.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                961,
                201,
                990,
                230
              ],
              "bboxDp": [
                349.455,
                73.091,
                360.0,
                83.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                943,
                714,
                990,
                746
              ],
              "bboxDp": [
                342.909,
                259.636,
                360.0,
                271.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                119,
                866,
                232,
                903
              ],
              "bboxDp": [
                43.273,
                314.909,
                84.364,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                466,
                866,
                540,
                903
              ],
              "bboxDp": [
                169.455,
                314.909,
                196.364,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                787,
                866,
                937,
                903
              ],
              "bboxDp": [
                286.182,
                314.909,
                340.727,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                75,
                947,
                113,
                1000
              ],
              "bboxDp": [
                27.273,
                344.364,
                41.091,
                363.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                405,
                947,
                443,
                1000
              ],
              "bboxDp": [
                147.273,
                344.364,
                161.091,
                363.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                735,
                947,
                773,
                1000
              ],
              "bboxDp": [
                267.273,
                344.364,
                281.091,
                363.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                140,
                1210,
                292,
                1247
              ],
              "bboxDp": [
                50.909,
                440.0,
                106.182,
                453.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                349,
                1210,
                384,
                1247
              ],
              "bboxDp": [
                126.909,
                440.0,
                139.636,
                453.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                310,
                1213,
                331,
                1243
              ],
              "bboxDp": [
                112.727,
                441.091,
                120.364,
                452.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                203,
                1426,
                374,
                1468
              ],
              "bboxDp": [
                73.818,
                518.545,
                136.0,
                533.818
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                202,
                1494,
                760,
                1525
              ],
              "bboxDp": [
                73.455,
                543.273,
                276.364,
                554.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                78,
                1764,
                168,
                1808
              ],
              "bboxDp": [
                28.364,
                641.455,
                61.091,
                657.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                612,
                1766,
                702,
                1811
              ],
              "bboxDp": [
                222.545,
                642.182,
                255.273,
                658.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                77,
                1830,
                385,
                1867
              ],
              "bboxDp": [
                28.0,
                665.455,
                140.0,
                678.909
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                612,
                1833,
                920,
                1870
              ],
              "bboxDp": [
                222.545,
                666.545,
                334.545,
                680.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        },
        "reference": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    136,
                    1080,
                    348
                  ],
                  "pixelArea": 215397,
                  "bboxDp": [
                    0.0,
                    49.455,
                    392.727,
                    126.545
                  ]
                },
                {
                  "bboxPx": [
                    948,
                    181,
                    1003,
                    236
                  ],
                  "pixelArea": 2007,
                  "bboxDp": [
                    344.727,
                    65.818,
                    364.727,
                    85.818
                  ]
                },
                {
                  "bboxPx": [
                    91,
                    187,
                    109,
                    257
                  ],
                  "pixelArea": 612,
                  "bboxDp": [
                    33.091,
                    68.0,
                    39.636,
                    93.455
                  ]
                },
                {
                  "bboxPx": [
                    136,
                    193,
                    144,
                    199
                  ],
                  "pixelArea": 48,
                  "bboxDp": [
                    49.455,
                    70.182,
                    52.364,
                    72.364
                  ]
                },
                {
                  "bboxPx": [
                    178,
                    194,
                    205,
                    212
                  ],
                  "pixelArea": 354,
                  "bboxDp": [
                    64.727,
                    70.545,
                    74.545,
                    77.091
                  ]
                },
                {
                  "bboxPx": [
                    136,
                    205,
                    144,
                    210
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    49.455,
                    74.545,
                    52.364,
                    76.364
                  ]
                },
                {
                  "bboxPx": [
                    213,
                    206,
                    229,
                    212
                  ],
                  "pixelArea": 96,
                  "bboxDp": [
                    77.455,
                    74.909,
                    83.273,
                    77.091
                  ]
                },
                {
                  "bboxPx": [
                    213,
                    218,
                    229,
                    223
                  ],
                  "pixelArea": 80,
                  "bboxDp": [
                    77.455,
                    79.273,
                    83.273,
                    81.091
                  ]
                },
                {
                  "bboxPx": [
                    188,
                    230,
                    205,
                    242
                  ],
                  "pixelArea": 118,
                  "bboxDp": [
                    68.364,
                    83.636,
                    74.545,
                    88.0
                  ]
                },
                {
                  "bboxPx": [
                    214,
                    230,
                    233,
                    240
                  ],
                  "pixelArea": 107,
                  "bboxDp": [
                    77.818,
                    83.636,
                    84.727,
                    87.273
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    797,
                    1047,
                    1320
                  ],
                  "pixelArea": 502121,
                  "bboxDp": [
                    12.0,
                    289.818,
                    380.727,
                    480.0
                  ]
                },
                {
                  "bboxPx": [
                    791,
                    877,
                    800,
                    886
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    287.636,
                    318.909,
                    290.909,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    804,
                    877,
                    814,
                    886
                  ],
                  "pixelArea": 90,
                  "bboxDp": [
                    292.364,
                    318.909,
                    296.0,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    172,
                    878,
                    184,
                    886
                  ],
                  "pixelArea": 53,
                  "bboxDp": [
                    62.545,
                    319.273,
                    66.909,
                    322.182
                  ]
                },
                {
                  "bboxPx": [
                    474,
                    882,
                    482,
                    892
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    172.364,
                    320.727,
                    175.273,
                    324.364
                  ]
                },
                {
                  "bboxPx": [
                    506,
                    884,
                    516,
                    899
                  ],
                  "pixelArea": 82,
                  "bboxDp": [
                    184.0,
                    321.455,
                    187.636,
                    326.909
                  ]
                },
                {
                  "bboxPx": [
                    182,
                    891,
                    191,
                    898
                  ],
                  "pixelArea": 63,
                  "bboxDp": [
                    66.182,
                    324.0,
                    69.455,
                    326.545
                  ]
                },
                {
                  "bboxPx": [
                    87,
                    956,
                    102,
                    990
                  ],
                  "pixelArea": 431,
                  "bboxDp": [
                    31.636,
                    347.636,
                    37.091,
                    360.0
                  ]
                },
                {
                  "bboxPx": [
                    417,
                    956,
                    432,
                    990
                  ],
                  "pixelArea": 433,
                  "bboxDp": [
                    151.636,
                    347.636,
                    157.091,
                    360.0
                  ]
                },
                {
                  "bboxPx": [
                    747,
                    956,
                    763,
                    990
                  ],
                  "pixelArea": 447,
                  "bboxDp": [
                    271.636,
                    347.636,
                    277.455,
                    360.0
                  ]
                },
                {
                  "bboxPx": [
                    305,
                    1218,
                    315,
                    1240
                  ],
                  "pixelArea": 181,
                  "bboxDp": [
                    110.909,
                    442.909,
                    114.545,
                    450.909
                  ]
                },
                {
                  "bboxPx": [
                    184,
                    1223,
                    200,
                    1239
                  ],
                  "pixelArea": 128,
                  "bboxDp": [
                    66.909,
                    444.727,
                    72.727,
                    450.545
                  ]
                },
                {
                  "bboxPx": [
                    264,
                    1224,
                    278,
                    1243
                  ],
                  "pixelArea": 105,
                  "bboxDp": [
                    96.0,
                    445.091,
                    101.091,
                    452.0
                  ]
                },
                {
                  "bboxPx": [
                    157,
                    1235,
                    169,
                    1241
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    57.091,
                    449.091,
                    61.455,
                    451.273
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1358,
                    1047,
                    1592
                  ],
                  "pixelArea": 214250,
                  "bboxDp": [
                    12.0,
                    493.818,
                    380.727,
                    578.909
                  ]
                },
                {
                  "bboxPx": [
                    313,
                    1427,
                    321,
                    1434
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    113.818,
                    518.909,
                    116.727,
                    521.455
                  ]
                },
                {
                  "bboxPx": [
                    120,
                    1435,
                    139,
                    1453
                  ],
                  "pixelArea": 263,
                  "bboxDp": [
                    43.636,
                    521.818,
                    50.545,
                    528.364
                  ]
                },
                {
                  "bboxPx": [
                    350,
                    1445,
                    359,
                    1452
                  ],
                  "pixelArea": 30,
                  "bboxDp": [
                    127.273,
                    525.455,
                    130.545,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    312,
                    1456,
                    321,
                    1461
                  ],
                  "pixelArea": 37,
                  "bboxDp": [
                    113.455,
                    529.455,
                    116.727,
                    531.273
                  ]
                },
                {
                  "bboxPx": [
                    111,
                    1460,
                    148,
                    1474
                  ],
                  "pixelArea": 470,
                  "bboxDp": [
                    40.364,
                    530.909,
                    53.818,
                    536.0
                  ]
                },
                {
                  "bboxPx": [
                    210,
                    1500,
                    218,
                    1512
                  ],
                  "pixelArea": 57,
                  "bboxDp": [
                    76.364,
                    545.455,
                    79.273,
                    549.818
                  ]
                },
                {
                  "bboxPx": [
                    605,
                    1501,
                    618,
                    1514
                  ],
                  "pixelArea": 76,
                  "bboxDp": [
                    220.0,
                    545.818,
                    224.727,
                    550.545
                  ]
                },
                {
                  "bboxPx": [
                    581,
                    1503,
                    589,
                    1511
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    211.273,
                    546.545,
                    214.182,
                    549.455
                  ]
                },
                {
                  "bboxPx": [
                    506,
                    1504,
                    518,
                    1512
                  ],
                  "pixelArea": 53,
                  "bboxDp": [
                    184.0,
                    546.909,
                    188.364,
                    549.818
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1619,
                    540,
                    2151
                  ],
                  "pixelArea": 207620,
                  "bboxDp": [
                    12.0,
                    588.727,
                    196.364,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    568,
                    1619,
                    1048,
                    2151
                  ],
                  "pixelArea": 228422,
                  "bboxDp": [
                    206.545,
                    588.727,
                    381.091,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    637,
                    1690,
                    659,
                    1708
                  ],
                  "pixelArea": 177,
                  "bboxDp": [
                    231.636,
                    614.545,
                    239.636,
                    621.091
                  ]
                },
                {
                  "bboxPx": [
                    98,
                    1693,
                    130,
                    1705
                  ],
                  "pixelArea": 242,
                  "bboxDp": [
                    35.636,
                    615.636,
                    47.273,
                    620.0
                  ]
                },
                {
                  "bboxPx": [
                    283,
                    1834,
                    296,
                    1839
                  ],
                  "pixelArea": 65,
                  "bboxDp": [
                    102.909,
                    666.909,
                    107.636,
                    668.727
                  ]
                },
                {
                  "bboxPx": [
                    729,
                    1839,
                    737,
                    1846
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    265.091,
                    668.727,
                    268.0,
                    671.273
                  ]
                },
                {
                  "bboxPx": [
                    314,
                    1843,
                    328,
                    1862
                  ],
                  "pixelArea": 109,
                  "bboxDp": [
                    114.182,
                    670.182,
                    119.273,
                    677.091
                  ]
                },
                {
                  "bboxPx": [
                    849,
                    1843,
                    857,
                    1850
                  ],
                  "pixelArea": 29,
                  "bboxDp": [
                    308.727,
                    670.182,
                    311.636,
                    672.727
                  ]
                },
                {
                  "bboxPx": [
                    120,
                    1847,
                    129,
                    1854
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    43.636,
                    671.636,
                    46.909,
                    674.182
                  ]
                },
                {
                  "bboxPx": [
                    134,
                    1847,
                    142,
                    1853
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    48.727,
                    671.636,
                    51.636,
                    673.818
                  ]
                },
                {
                  "bboxPx": [
                    655,
                    1847,
                    664,
                    1854
                  ],
                  "pixelArea": 37,
                  "bboxDp": [
                    238.182,
                    671.636,
                    241.455,
                    674.182
                  ]
                },
                {
                  "bboxPx": [
                    669,
                    1847,
                    677,
                    1853
                  ],
                  "pixelArea": 30,
                  "bboxDp": [
                    243.273,
                    671.636,
                    246.182,
                    673.818
                  ]
                },
                {
                  "bboxPx": [
                    853,
                    1847,
                    863,
                    1862
                  ],
                  "pixelArea": 78,
                  "bboxDp": [
                    310.182,
                    671.636,
                    313.818,
                    677.091
                  ]
                },
                {
                  "bboxPx": [
                    729,
                    1850,
                    737,
                    1858
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    265.091,
                    672.727,
                    268.0,
                    675.636
                  ]
                },
                {
                  "bboxPx": [
                    283,
                    1851,
                    291,
                    1860
                  ],
                  "pixelArea": 63,
                  "bboxDp": [
                    102.909,
                    673.091,
                    105.818,
                    676.364
                  ]
                },
                {
                  "bboxPx": [
                    362,
                    1856,
                    372,
                    1861
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    131.636,
                    674.909,
                    135.273,
                    676.727
                  ]
                },
                {
                  "bboxPx": [
                    897,
                    1856,
                    907,
                    1861
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    326.182,
                    674.909,
                    329.818,
                    676.727
                  ]
                },
                {
                  "bboxPx": [
                    0,
                    2152,
                    1080,
                    2334
                  ],
                  "pixelArea": 179900,
                  "bboxDp": [
                    0.0,
                    782.545,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    119,
                    2203,
                    152,
                    2237
                  ],
                  "pixelArea": 842,
                  "bboxDp": [
                    43.273,
                    801.091,
                    55.273,
                    813.455
                  ]
                },
                {
                  "bboxPx": [
                    674,
                    2208,
                    687,
                    2221
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    245.091,
                    802.909,
                    249.818,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    659,
                    2274,
                    669,
                    2282
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    239.636,
                    826.909,
                    243.273,
                    829.818
                  ]
                },
                {
                  "bboxPx": [
                    412,
                    2280,
                    423,
                    2289
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    149.818,
                    829.091,
                    153.818,
                    832.364
                  ]
                }
              ],
              "totalQualifyingRegions": 56
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    136
                  ],
                  "pixelArea": 43200,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    49.455
                  ]
                },
                {
                  "bboxPx": [
                    0,
                    797,
                    1080,
                    2151
                  ],
                  "pixelArea": 173646,
                  "bboxDp": [
                    0.0,
                    289.818,
                    392.727,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    74,
                    1999,
                    499,
                    2109
                  ],
                  "pixelArea": 46614,
                  "bboxDp": [
                    26.909,
                    726.909,
                    181.455,
                    766.909
                  ]
                }
              ],
              "totalQualifyingRegions": 3
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    103,
                    2187,
                    168,
                    2252
                  ],
                  "pixelArea": 2317,
                  "bboxDp": [
                    37.455,
                    795.273,
                    61.091,
                    818.909
                  ]
                },
                {
                  "bboxPx": [
                    121,
                    2269,
                    134,
                    2294
                  ],
                  "pixelArea": 142,
                  "bboxDp": [
                    44.0,
                    825.091,
                    48.727,
                    834.182
                  ]
                },
                {
                  "bboxPx": [
                    137,
                    2269,
                    164,
                    2297
                  ],
                  "pixelArea": 336,
                  "bboxDp": [
                    49.818,
                    825.091,
                    59.636,
                    835.273
                  ]
                },
                {
                  "bboxPx": [
                    113,
                    2271,
                    133,
                    2297
                  ],
                  "pixelArea": 113,
                  "bboxDp": [
                    41.091,
                    825.818,
                    48.364,
                    835.273
                  ]
                }
              ],
              "totalQualifyingRegions": 4
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                943,
                176,
                1008,
                241
              ],
              "bboxDp": [
                342.909,
                64.0,
                366.545,
                87.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                75,
                179,
                244,
                262
              ],
              "bboxDp": [
                27.273,
                65.091,
                88.727,
                95.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                961,
                194,
                990,
                223
              ],
              "bboxDp": [
                349.455,
                70.545,
                360.0,
                81.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                950,
                728,
                996,
                759
              ],
              "bboxDp": [
                345.455,
                264.727,
                362.182,
                276.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                124,
                866,
                234,
                903
              ],
              "bboxDp": [
                45.091,
                314.909,
                85.091,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                454,
                866,
                527,
                903
              ],
              "bboxDp": [
                165.091,
                314.909,
                191.636,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                787,
                866,
                933,
                903
              ],
              "bboxDp": [
                286.182,
                314.909,
                339.273,
                328.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                75,
                946,
                113,
                999
              ],
              "bboxDp": [
                27.273,
                344.0,
                41.091,
                363.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                405,
                946,
                443,
                999
              ],
              "bboxDp": [
                147.273,
                344.0,
                161.091,
                363.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                736,
                946,
                774,
                999
              ],
              "bboxDp": [
                267.636,
                344.0,
                281.455,
                363.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                140,
                1210,
                289,
                1247
              ],
              "bboxDp": [
                50.909,
                440.0,
                105.091,
                453.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                331,
                1210,
                365,
                1247
              ],
              "bboxDp": [
                120.364,
                440.0,
                132.727,
                453.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                301,
                1215,
                319,
                1243
              ],
              "bboxDp": [
                109.455,
                441.818,
                116.0,
                452.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                203,
                1422,
                383,
                1467
              ],
              "bboxDp": [
                73.818,
                517.091,
                139.273,
                533.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                202,
                1490,
                760,
                1521
              ],
              "bboxDp": [
                73.455,
                541.818,
                276.364,
                553.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                611,
                1761,
                700,
                1806
              ],
              "bboxDp": [
                222.182,
                640.364,
                254.545,
                656.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                77,
                1762,
                165,
                1806
              ],
              "bboxDp": [
                28.0,
                640.727,
                60.0,
                656.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                76,
                1829,
                377,
                1866
              ],
              "bboxDp": [
                27.636,
                665.091,
                137.091,
                678.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                611,
                1829,
                912,
                1866
              ],
              "bboxDp": [
                222.182,
                665.091,
                331.636,
                678.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        }
      },
      "inkPositionCandidates": [
        {
          "originalRegion": 0,
          "referenceRegion": 1,
          "originalBboxPx": [
            75,
            179,
            244,
            262
          ],
          "referenceBboxPx": [
            75,
            179,
            244,
            262
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 1,
          "referenceRegion": 0,
          "originalBboxPx": [
            943,
            183,
            1008,
            248
          ],
          "referenceBboxPx": [
            943,
            176,
            1008,
            241
          ],
          "edgeDeltaPx": [
            0,
            -7,
            0,
            -7
          ],
          "edgeDeltaDp": [
            0.0,
            -2.545,
            0.0,
            -2.545
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 2,
          "referenceRegion": 2,
          "originalBboxPx": [
            961,
            201,
            990,
            230
          ],
          "referenceBboxPx": [
            961,
            194,
            990,
            223
          ],
          "edgeDeltaPx": [
            0,
            -7,
            0,
            -7
          ],
          "edgeDeltaDp": [
            0.0,
            -2.545,
            0.0,
            -2.545
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 3,
          "referenceRegion": 3,
          "originalBboxPx": [
            943,
            714,
            990,
            746
          ],
          "referenceBboxPx": [
            950,
            728,
            996,
            759
          ],
          "edgeDeltaPx": [
            7,
            14,
            6,
            13
          ],
          "edgeDeltaDp": [
            2.545,
            5.091,
            2.182,
            4.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 4,
          "referenceRegion": 4,
          "originalBboxPx": [
            119,
            866,
            232,
            903
          ],
          "referenceBboxPx": [
            124,
            866,
            234,
            903
          ],
          "edgeDeltaPx": [
            5,
            0,
            2,
            0
          ],
          "edgeDeltaDp": [
            1.818,
            0.0,
            0.727,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 5,
          "referenceRegion": 5,
          "originalBboxPx": [
            466,
            866,
            540,
            903
          ],
          "referenceBboxPx": [
            454,
            866,
            527,
            903
          ],
          "edgeDeltaPx": [
            -12,
            0,
            -13,
            0
          ],
          "edgeDeltaDp": [
            -4.364,
            0.0,
            -4.727,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 6,
          "referenceRegion": 6,
          "originalBboxPx": [
            787,
            866,
            937,
            903
          ],
          "referenceBboxPx": [
            787,
            866,
            933,
            903
          ],
          "edgeDeltaPx": [
            0,
            0,
            -4,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            -1.455,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 7,
          "referenceRegion": 7,
          "originalBboxPx": [
            75,
            947,
            113,
            1000
          ],
          "referenceBboxPx": [
            75,
            946,
            113,
            999
          ],
          "edgeDeltaPx": [
            0,
            -1,
            0,
            -1
          ],
          "edgeDeltaDp": [
            0.0,
            -0.364,
            0.0,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 8,
          "referenceRegion": 8,
          "originalBboxPx": [
            405,
            947,
            443,
            1000
          ],
          "referenceBboxPx": [
            405,
            946,
            443,
            999
          ],
          "edgeDeltaPx": [
            0,
            -1,
            0,
            -1
          ],
          "edgeDeltaDp": [
            0.0,
            -0.364,
            0.0,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 9,
          "referenceRegion": 9,
          "originalBboxPx": [
            735,
            947,
            773,
            1000
          ],
          "referenceBboxPx": [
            736,
            946,
            774,
            999
          ],
          "edgeDeltaPx": [
            1,
            -1,
            1,
            -1
          ],
          "edgeDeltaDp": [
            0.364,
            -0.364,
            0.364,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 10,
          "referenceRegion": 10,
          "originalBboxPx": [
            140,
            1210,
            292,
            1247
          ],
          "referenceBboxPx": [
            140,
            1210,
            289,
            1247
          ],
          "edgeDeltaPx": [
            0,
            0,
            -3,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            -1.091,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 11,
          "referenceRegion": 11,
          "originalBboxPx": [
            349,
            1210,
            384,
            1247
          ],
          "referenceBboxPx": [
            331,
            1210,
            365,
            1247
          ],
          "edgeDeltaPx": [
            -18,
            0,
            -19,
            0
          ],
          "edgeDeltaDp": [
            -6.545,
            0.0,
            -6.909,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 12,
          "referenceRegion": 12,
          "originalBboxPx": [
            310,
            1213,
            331,
            1243
          ],
          "referenceBboxPx": [
            301,
            1215,
            319,
            1243
          ],
          "edgeDeltaPx": [
            -9,
            2,
            -12,
            0
          ],
          "edgeDeltaDp": [
            -3.273,
            0.727,
            -4.364,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 13,
          "referenceRegion": 13,
          "originalBboxPx": [
            203,
            1426,
            374,
            1468
          ],
          "referenceBboxPx": [
            203,
            1422,
            383,
            1467
          ],
          "edgeDeltaPx": [
            0,
            -4,
            9,
            -1
          ],
          "edgeDeltaDp": [
            0.0,
            -1.455,
            3.273,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 14,
          "referenceRegion": 14,
          "originalBboxPx": [
            202,
            1494,
            760,
            1525
          ],
          "referenceBboxPx": [
            202,
            1490,
            760,
            1521
          ],
          "edgeDeltaPx": [
            0,
            -4,
            0,
            -4
          ],
          "edgeDeltaDp": [
            0.0,
            -1.455,
            0.0,
            -1.455
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 15,
          "referenceRegion": 16,
          "originalBboxPx": [
            78,
            1764,
            168,
            1808
          ],
          "referenceBboxPx": [
            77,
            1762,
            165,
            1806
          ],
          "edgeDeltaPx": [
            -1,
            -2,
            -3,
            -2
          ],
          "edgeDeltaDp": [
            -0.364,
            -0.727,
            -1.091,
            -0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 16,
          "referenceRegion": 15,
          "originalBboxPx": [
            612,
            1766,
            702,
            1811
          ],
          "referenceBboxPx": [
            611,
            1761,
            700,
            1806
          ],
          "edgeDeltaPx": [
            -1,
            -5,
            -2,
            -5
          ],
          "edgeDeltaDp": [
            -0.364,
            -1.818,
            -0.727,
            -1.818
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 17,
          "referenceRegion": 17,
          "originalBboxPx": [
            77,
            1830,
            385,
            1867
          ],
          "referenceBboxPx": [
            76,
            1829,
            377,
            1866
          ],
          "edgeDeltaPx": [
            -1,
            -1,
            -8,
            -1
          ],
          "edgeDeltaDp": [
            -0.364,
            -0.364,
            -2.909,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 18,
          "referenceRegion": 18,
          "originalBboxPx": [
            612,
            1833,
            920,
            1870
          ],
          "referenceBboxPx": [
            611,
            1829,
            912,
            1866
          ],
          "edgeDeltaPx": [
            -1,
            -4,
            -8,
            -4
          ],
          "edgeDeltaDp": [
            -0.364,
            -1.455,
            -2.909,
            -1.455
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        }
      ],
      "manualReviewInputMatches": true,
      "components": [
        {
          "id": "TopBar",
          "page": "health-day",
          "cropBoxPx": [
            0,
            136,
            1080,
            301
          ],
          "sourceBoundsPx": [
            0,
            136,
            1080,
            301
          ],
          "basis": "health title [72,151,248,280] plus add action [926,166,1025,265]; enclosing top bar context",
          "notes": "Health variant only; Settings expanded and Mine blank-title variants remain in full-page comparison.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
            "reference": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99"
          },
          "originalEvidenceId": "ev_716b470d4284b551bafa1e3ddb5f5493f9a6e535fb6c1065b73866e2777c7963",
          "referenceEvidenceId": "ev_f55fe72599012450e78da26423dd5fea6f6392270cc325a9440c8729161265e6",
          "images": {
            "sideBySide": {
              "path": "generated/component-TopBar-side-by-side.png",
              "sha256": "f9df7e5581e1fbcebe5337e8eae5774c4e3e94900f3949ad0ae402e5ff76070b",
              "canvasPx": [
                2160,
                165
              ]
            },
            "overlay": {
              "path": "generated/component-TopBar-overlay.png",
              "sha256": "dc2217c7c2da958b571cab8844a4801672f1632e32df53fafea3ec9bf266f97b",
              "canvasPx": [
                1080,
                165
              ]
            },
            "difference": {
              "path": "generated/component-TopBar-difference-x3.png",
              "sha256": "e22f6672fc892177eeeecc60ea6809bb9a461d475d51924dcb45d07b241edd77",
              "canvasPx": [
                1080,
                165
              ]
            }
          }
        },
        {
          "id": "BottomNav",
          "page": "health-day",
          "cropBoxPx": [
            0,
            2143,
            1080,
            2334
          ],
          "sourceBoundsPx": [
            0,
            2151,
            1080,
            2334
          ],
          "basis": "spec.md health fixed bottom navigation; 8px context above",
          "notes": "Default selected Health state; no animation inferred.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
            "reference": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99"
          },
          "originalEvidenceId": "ev_716b470d4284b551bafa1e3ddb5f5493f9a6e535fb6c1065b73866e2777c7963",
          "referenceEvidenceId": "ev_f55fe72599012450e78da26423dd5fea6f6392270cc325a9440c8729161265e6",
          "images": {
            "sideBySide": {
              "path": "generated/component-BottomNav-side-by-side.png",
              "sha256": "f69385fa271e5fee1abddedc77f7433f5b9ca9ab030c26eccb878a328653e94a",
              "canvasPx": [
                2160,
                191
              ]
            },
            "overlay": {
              "path": "generated/component-BottomNav-overlay.png",
              "sha256": "ab97fcffde860c8fa6a1a0a4b6660d61fde782796aed8aa5406568cf57a36d47",
              "canvasPx": [
                1080,
                191
              ]
            },
            "difference": {
              "path": "generated/component-BottomNav-difference-x3.png",
              "sha256": "cef5b36098a8209d087f2e5b9d7a41c934d5e060037d71bb0dd492086068d454",
              "canvasPx": [
                1080,
                191
              ]
            }
          }
        },
        {
          "id": "ProgressHero",
          "page": "health-day",
          "cropBoxPx": [
            0,
            327,
            1080,
            1328
          ],
          "sourceBoundsPx": [
            0,
            335,
            1080,
            1319
          ],
          "basis": "Hero rings/reflection/share plus three-summary surface [33,797,1047,1319] with 8px context",
          "notes": "Composite component in zero-progress guest state. Reflection is a reconstruction choice; nonzero progress remains unknown.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
            "reference": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99"
          },
          "originalEvidenceId": "ev_716b470d4284b551bafa1e3ddb5f5493f9a6e535fb6c1065b73866e2777c7963",
          "referenceEvidenceId": "ev_f55fe72599012450e78da26423dd5fea6f6392270cc325a9440c8729161265e6",
          "images": {
            "sideBySide": {
              "path": "generated/component-ProgressHero-side-by-side.png",
              "sha256": "c9b6cd2765db6a4a57b3e6b21ce7c5f5116c6e7e13f976cde5e163ac9d84740e",
              "canvasPx": [
                2160,
                1001
              ]
            },
            "overlay": {
              "path": "generated/component-ProgressHero-overlay.png",
              "sha256": "f55b75d92b2c184ea1d162e1d4a02bcb652c0adb39aa9090f9e1bf2461b1fb30",
              "canvasPx": [
                1080,
                1001
              ]
            },
            "difference": {
              "path": "generated/component-ProgressHero-difference-x3.png",
              "sha256": "a621173b80347ad37992908f67ad43cfe6c9aac3a40afbfaa14ab24feeaab40a",
              "canvasPx": [
                1080,
                1001
              ]
            }
          }
        },
        {
          "id": "MetricCard",
          "page": "health-day",
          "cropBoxPx": [
            25,
            1611,
            548,
            2151
          ],
          "sourceBoundsPx": [
            33,
            1620,
            540,
            2150
          ],
          "basis": "Visible left sleep card exact white extent, with context",
          "notes": "Only the visible part above the fixed navigation is observed; this is not evidence of full card height.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "a932b00337f9d4cd71516d5b9d55451cc0af1841d7935c3ca8c487005c2e2166",
            "reference": "92a90a75aa3260bb1eaa301ff0395aeaa5062fca486e606f8f9c83ce24c21a99"
          },
          "originalEvidenceId": "ev_716b470d4284b551bafa1e3ddb5f5493f9a6e535fb6c1065b73866e2777c7963",
          "referenceEvidenceId": "ev_f55fe72599012450e78da26423dd5fea6f6392270cc325a9440c8729161265e6",
          "images": {
            "sideBySide": {
              "path": "generated/component-MetricCard-side-by-side.png",
              "sha256": "b48f5e457c5a7d5fa20b4d311e5cab429226ef030ac756dbe3ccf3aeb95b025a",
              "canvasPx": [
                1046,
                540
              ]
            },
            "overlay": {
              "path": "generated/component-MetricCard-overlay.png",
              "sha256": "5eafedcb7ed54e3aa5cf999a2a9fd8b93e72f9c3ad861bcaf92abf2109b02684",
              "canvasPx": [
                523,
                540
              ]
            },
            "difference": {
              "path": "generated/component-MetricCard-difference-x3.png",
              "sha256": "92dc27b4c64e31f2096b125db5b46f41d2181d81ed495b90904d4bbafe96bfa6",
              "canvasPx": [
                523,
                540
              ]
            }
          }
        }
      ]
    },
    {
      "id": "mine-day",
      "label": "我的",
      "sources": {
        "original": {
          "path": "../runtime/mine-day/screen.png",
          "sha256": "c8857401ecf63646478bfd7b522b243bf112ed10fb80ee238e9bc11e7f067348",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        },
        "reference": {
          "path": "../reference/mine-day/screen.png",
          "sha256": "315bfedfdec46529b86bf1d7fc188d881a68c07b99211ac56eade4d4c7125ab6",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        }
      },
      "originalEvidence": {
        "evidenceId": "ev_1d57d0e878ed7ab61a9f841d5766bacd63d04fe53bc3af5fed5b36a9420e86d8",
        "record": "../runtime/minecap.rea.json",
        "expectedSha256": "c8857401ecf63646478bfd7b522b243bf112ed10fb80ee238e9bc11e7f067348",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "referenceEvidence": {
        "evidenceId": "ev_6169a1dfb3c6ff777e10df791be209bc1dd23343dbb21f6191f0763f7feae694",
        "record": "../reference/mine-day.rea.json",
        "expectedSha256": "315bfedfdec46529b86bf1d7fc188d881a68c07b99211ac56eade4d4c7125ab6",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "images": {
        "sideBySide": {
          "path": "generated/mine-day-side-by-side.png",
          "sha256": "8d199f3a918c21dc5090946ceb112e2a238283f6c14e5a52a33b997482e3d7e4",
          "canvasPx": [
            2160,
            2400
          ]
        },
        "overlay": {
          "path": "generated/mine-day-overlay.png",
          "sha256": "abfdd7f1e9e5bc953e391928f914b88f1116cdaeebe30350d45d6da5e8dc3432",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "difference": {
          "path": "generated/mine-day-difference-x3.png",
          "sha256": "3c5d566d1dd9fe831cd171b87bf653ba38d963b2b9c16b8aa8de5ffba2cc9d1d",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "rawDifference": {
          "path": "generated/mine-day-difference-raw.png",
          "sha256": "379553cdf26ff207842bb1153246e970f71a2cbabb2206772b7239829cf829c2",
          "canvasPx": [
            1080,
            2400
          ]
        }
      },
      "overlayBlendWeight": 0.5,
      "differenceDisplayAmplification": 3,
      "knownDifferences": [
        {
          "component": "ProfileHeader",
          "classification": "D versus A",
          "observation": "姓名A[242,319,401,370]→D[242,323,401,374]，D下移4px；说明A[241,387,402,418]→D[241,390,402,421]，下移3px。尾部徽章大小/透明层也有重影。"
        },
        {
          "component": "ShortcutGrid",
          "classification": "D versus A",
          "observation": "首项文字A[58,602,210,639]→D[60,616,208,653]，下移14px且略窄；其余入口也有同向文字行位差。个别图标约下移2px。",
          "impact": "顺序和等宽分配对应，图文间距仍需修正。"
        },
        {
          "component": "SurfaceCard/promo",
          "classification": "D versus A",
          "observation": "会员横幅D缺少A右上的斜向明暗图案；会员文案、权益文案和开通动作的排版/字形仍有差。运营插画和医疗权益不作为通用迁移规则。"
        },
        {
          "component": "SurfaceCard/promo title",
          "classification": "D versus A",
          "observation": "小习惯标题A[74,1163,211,1208]→D[74,1166,209,1211]；周报A[595,1163,873,1208]→D[595,1166,868,1211]。D下移3px且略窄，副文行位和图标尺寸也有差。"
        },
        {
          "component": "ChevronRow",
          "classification": "D versus A",
          "observation": "任务中心白面A[33,1612,1047,1799]→D[33,1611,1047,1798]；健康问诊A[33,1832,1047,1997]高165px，D[33,1831,1047,2018]高187px。D错误套68dp而A该行约60dp，下一行从A2030变成D2051。",
          "impact": "错误行高会累积滚动位置偏差，应按行实例和分组设置。"
        },
        {
          "component": "BottomNav",
          "classification": "D versus A",
          "observation": "D白面起点比A低1px；“我的”文字D约上移4px。相同资源仍有栅格尺寸/位置差异。"
        }
      ],
      "matchedDetails": [
        "采用mine11.202.21的五入口顺序：活动、课程、亲友、奖品、订单；页头保持空白。",
        "小习惯白面A[33,1105,526,1579]→D[33,1105,526,1578]；周报A/D[554,1105,1047,1328]；竞赛A/D[554,1356,1047,1579]。",
        "当前样本中#F6F7F9背景、白面、12dp外侧间距和10dp两列间隙对应；内部图文仍分别核对。"
      ],
      "unknowns": [
        "登录后资料、长名字、大字与更多入口未对照。",
        "会员、医疗和任务状态属健康业务专属内容。",
        "完整菜单每一行和滚动/回弹/导航动画未由此静帧验证。"
      ],
      "manualReviewStatus": "reviewed-with-differences",
      "regions": {
        "original": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    912,
                    321,
                    1011,
                    409
                  ],
                  "pixelArea": 4561,
                  "bboxDp": [
                    331.636,
                    116.727,
                    367.636,
                    148.727
                  ]
                },
                {
                  "bboxPx": [
                    127,
                    333,
                    152,
                    357
                  ],
                  "pixelArea": 210,
                  "bboxDp": [
                    46.182,
                    121.091,
                    55.273,
                    129.818
                  ]
                },
                {
                  "bboxPx": [
                    127,
                    354,
                    148,
                    363
                  ],
                  "pixelArea": 82,
                  "bboxDp": [
                    46.182,
                    128.727,
                    53.818,
                    132.0
                  ]
                },
                {
                  "bboxPx": [
                    112,
                    373,
                    121,
                    380
                  ],
                  "pixelArea": 53,
                  "bboxDp": [
                    40.727,
                    135.636,
                    44.0,
                    138.182
                  ]
                },
                {
                  "bboxPx": [
                    125,
                    373,
                    168,
                    397
                  ],
                  "pixelArea": 748,
                  "bboxDp": [
                    45.455,
                    135.636,
                    61.091,
                    144.364
                  ]
                },
                {
                  "bboxPx": [
                    745,
                    524,
                    765,
                    531
                  ],
                  "pixelArea": 122,
                  "bboxDp": [
                    270.909,
                    190.545,
                    278.182,
                    193.091
                  ]
                },
                {
                  "bboxPx": [
                    722,
                    538,
                    762,
                    560
                  ],
                  "pixelArea": 774,
                  "bboxDp": [
                    262.545,
                    195.636,
                    277.091,
                    203.636
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1105,
                    526,
                    1579
                  ],
                  "pixelArea": 226471,
                  "bboxDp": [
                    12.0,
                    401.818,
                    191.273,
                    574.182
                  ]
                },
                {
                  "bboxPx": [
                    554,
                    1105,
                    1047,
                    1328
                  ],
                  "pixelArea": 92259,
                  "bboxDp": [
                    201.455,
                    401.818,
                    380.727,
                    482.909
                  ]
                },
                {
                  "bboxPx": [
                    745,
                    1172,
                    757,
                    1180
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    270.909,
                    426.182,
                    275.273,
                    429.091
                  ]
                },
                {
                  "bboxPx": [
                    708,
                    1174,
                    717,
                    1180
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    257.455,
                    426.909,
                    260.727,
                    429.091
                  ]
                },
                {
                  "bboxPx": [
                    651,
                    1179,
                    668,
                    1196
                  ],
                  "pixelArea": 118,
                  "bboxDp": [
                    236.727,
                    428.727,
                    242.909,
                    434.909
                  ]
                },
                {
                  "bboxPx": [
                    671,
                    1179,
                    679,
                    1201
                  ],
                  "pixelArea": 104,
                  "bboxDp": [
                    244.0,
                    428.727,
                    246.909,
                    436.727
                  ]
                },
                {
                  "bboxPx": [
                    618,
                    1182,
                    628,
                    1191
                  ],
                  "pixelArea": 47,
                  "bboxDp": [
                    224.727,
                    429.818,
                    228.364,
                    433.091
                  ]
                },
                {
                  "bboxPx": [
                    698,
                    1183,
                    706,
                    1191
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    253.818,
                    430.182,
                    256.727,
                    433.091
                  ]
                },
                {
                  "bboxPx": [
                    709,
                    1185,
                    717,
                    1201
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    257.818,
                    430.909,
                    260.727,
                    436.727
                  ]
                },
                {
                  "bboxPx": [
                    743,
                    1185,
                    757,
                    1200
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    270.182,
                    430.909,
                    275.273,
                    436.364
                  ]
                },
                {
                  "bboxPx": [
                    190,
                    1188,
                    202,
                    1199
                  ],
                  "pixelArea": 67,
                  "bboxDp": [
                    69.091,
                    432.0,
                    73.455,
                    436.0
                  ]
                },
                {
                  "bboxPx": [
                    954,
                    1210,
                    987,
                    1227
                  ],
                  "pixelArea": 134,
                  "bboxDp": [
                    346.909,
                    440.0,
                    358.909,
                    446.182
                  ]
                },
                {
                  "bboxPx": [
                    621,
                    1248,
                    631,
                    1270
                  ],
                  "pixelArea": 184,
                  "bboxDp": [
                    225.818,
                    453.818,
                    229.455,
                    461.818
                  ]
                },
                {
                  "bboxPx": [
                    653,
                    1248,
                    663,
                    1270
                  ],
                  "pixelArea": 184,
                  "bboxDp": [
                    237.455,
                    453.818,
                    241.091,
                    461.818
                  ]
                },
                {
                  "bboxPx": [
                    766,
                    1248,
                    776,
                    1270
                  ],
                  "pixelArea": 184,
                  "bboxDp": [
                    278.545,
                    453.818,
                    282.182,
                    461.818
                  ]
                },
                {
                  "bboxPx": [
                    554,
                    1356,
                    1047,
                    1579
                  ],
                  "pixelArea": 94911,
                  "bboxDp": [
                    201.455,
                    493.091,
                    380.727,
                    574.182
                  ]
                },
                {
                  "bboxPx": [
                    617,
                    1445,
                    630,
                    1451
                  ],
                  "pixelArea": 78,
                  "bboxDp": [
                    224.364,
                    525.455,
                    229.091,
                    527.636
                  ]
                },
                {
                  "bboxPx": [
                    961,
                    1445,
                    980,
                    1465
                  ],
                  "pixelArea": 201,
                  "bboxDp": [
                    349.455,
                    525.455,
                    356.364,
                    532.727
                  ]
                },
                {
                  "bboxPx": [
                    617,
                    1498,
                    625,
                    1519
                  ],
                  "pixelArea": 168,
                  "bboxDp": [
                    224.364,
                    544.727,
                    227.273,
                    552.364
                  ]
                },
                {
                  "bboxPx": [
                    805,
                    1500,
                    817,
                    1510
                  ],
                  "pixelArea": 108,
                  "bboxDp": [
                    292.727,
                    545.455,
                    297.091,
                    549.091
                  ]
                },
                {
                  "bboxPx": [
                    759,
                    1504,
                    770,
                    1516
                  ],
                  "pixelArea": 88,
                  "bboxDp": [
                    276.0,
                    546.909,
                    280.0,
                    551.273
                  ]
                },
                {
                  "bboxPx": [
                    687,
                    1509,
                    699,
                    1517
                  ],
                  "pixelArea": 58,
                  "bboxDp": [
                    249.818,
                    548.727,
                    254.182,
                    551.636
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1612,
                    1047,
                    1799
                  ],
                  "pixelArea": 179304,
                  "bboxDp": [
                    12.0,
                    586.182,
                    380.727,
                    654.182
                  ]
                },
                {
                  "bboxPx": [
                    108,
                    1692,
                    122,
                    1708
                  ],
                  "pixelArea": 116,
                  "bboxDp": [
                    39.273,
                    615.273,
                    44.364,
                    621.091
                  ]
                },
                {
                  "bboxPx": [
                    274,
                    1699,
                    283,
                    1707
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    99.636,
                    617.818,
                    102.909,
                    620.727
                  ]
                },
                {
                  "bboxPx": [
                    289,
                    1699,
                    298,
                    1707
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    105.091,
                    617.818,
                    108.364,
                    620.727
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1832,
                    1047,
                    1997
                  ],
                  "pixelArea": 155903,
                  "bboxDp": [
                    12.0,
                    666.182,
                    380.727,
                    726.182
                  ]
                },
                {
                  "bboxPx": [
                    230,
                    1903,
                    242,
                    1909
                  ],
                  "pixelArea": 22,
                  "bboxDp": [
                    83.636,
                    692.0,
                    88.0,
                    694.182
                  ]
                },
                {
                  "bboxPx": [
                    196,
                    1923,
                    204,
                    1930
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    71.273,
                    699.273,
                    74.182,
                    701.818
                  ]
                },
                {
                  "bboxPx": [
                    107,
                    1925,
                    120,
                    1932
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    38.909,
                    700.0,
                    43.636,
                    702.545
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    2030,
                    1047,
                    2150
                  ],
                  "pixelArea": 110372,
                  "bboxDp": [
                    12.0,
                    738.182,
                    380.727,
                    781.818
                  ]
                },
                {
                  "bboxPx": [
                    192,
                    2108,
                    200,
                    2115
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    69.818,
                    766.545,
                    72.727,
                    769.091
                  ]
                },
                {
                  "bboxPx": [
                    245,
                    2115,
                    256,
                    2130
                  ],
                  "pixelArea": 92,
                  "bboxDp": [
                    89.091,
                    769.091,
                    93.091,
                    774.545
                  ]
                },
                {
                  "bboxPx": [
                    97,
                    2116,
                    119,
                    2139
                  ],
                  "pixelArea": 192,
                  "bboxDp": [
                    35.273,
                    769.455,
                    43.273,
                    777.818
                  ]
                },
                {
                  "bboxPx": [
                    320,
                    2116,
                    333,
                    2126
                  ],
                  "pixelArea": 48,
                  "bboxDp": [
                    116.364,
                    769.455,
                    121.091,
                    773.091
                  ]
                },
                {
                  "bboxPx": [
                    279,
                    2118,
                    291,
                    2125
                  ],
                  "pixelArea": 47,
                  "bboxDp": [
                    101.455,
                    770.182,
                    105.818,
                    772.727
                  ]
                },
                {
                  "bboxPx": [
                    362,
                    2121,
                    377,
                    2134
                  ],
                  "pixelArea": 74,
                  "bboxDp": [
                    131.636,
                    771.273,
                    137.091,
                    776.0
                  ]
                },
                {
                  "bboxPx": [
                    0,
                    2151,
                    1080,
                    2334
                  ],
                  "pixelArea": 181945,
                  "bboxDp": [
                    0.0,
                    782.182,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    120,
                    2204,
                    150,
                    2234
                  ],
                  "pixelArea": 660,
                  "bboxDp": [
                    43.636,
                    801.455,
                    54.545,
                    812.364
                  ]
                },
                {
                  "bboxPx": [
                    674,
                    2207,
                    687,
                    2220
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    245.091,
                    802.545,
                    249.818,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    413,
                    2281,
                    423,
                    2291
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    150.182,
                    829.455,
                    153.818,
                    833.091
                  ]
                }
              ],
              "totalQualifyingRegions": 48
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    2150
                  ],
                  "pixelArea": 877338,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    781.818
                  ]
                },
                {
                  "bboxPx": [
                    949,
                    182,
                    1004,
                    237
                  ],
                  "pixelArea": 2012,
                  "bboxDp": [
                    345.091,
                    66.182,
                    365.091,
                    86.182
                  ]
                },
                {
                  "bboxPx": [
                    817,
                    186,
                    872,
                    235
                  ],
                  "pixelArea": 2120,
                  "bboxDp": [
                    297.091,
                    67.636,
                    317.091,
                    85.455
                  ]
                },
                {
                  "bboxPx": [
                    310,
                    347,
                    332,
                    353
                  ],
                  "pixelArea": 132,
                  "bboxDp": [
                    112.727,
                    126.182,
                    120.727,
                    128.364
                  ]
                },
                {
                  "bboxPx": [
                    314,
                    357,
                    329,
                    365
                  ],
                  "pixelArea": 94,
                  "bboxDp": [
                    114.182,
                    129.818,
                    119.636,
                    132.727
                  ]
                },
                {
                  "bboxPx": [
                    924,
                    514,
                    966,
                    562
                  ],
                  "pixelArea": 1450,
                  "bboxDp": [
                    336.0,
                    186.909,
                    351.273,
                    204.364
                  ]
                },
                {
                  "bboxPx": [
                    112,
                    516,
                    135,
                    534
                  ],
                  "pixelArea": 412,
                  "bboxDp": [
                    40.727,
                    187.636,
                    49.091,
                    194.182
                  ]
                },
                {
                  "bboxPx": [
                    310,
                    516,
                    364,
                    560
                  ],
                  "pixelArea": 1797,
                  "bboxDp": [
                    112.727,
                    187.636,
                    132.364,
                    203.636
                  ]
                },
                {
                  "bboxPx": [
                    516,
                    516,
                    565,
                    543
                  ],
                  "pixelArea": 1019,
                  "bboxDp": [
                    187.636,
                    187.636,
                    205.455,
                    197.455
                  ]
                },
                {
                  "bboxPx": [
                    132,
                    528,
                    152,
                    546
                  ],
                  "pixelArea": 213,
                  "bboxDp": [
                    48.0,
                    192.0,
                    55.273,
                    198.545
                  ]
                },
                {
                  "bboxPx": [
                    333,
                    531,
                    343,
                    545
                  ],
                  "pixelArea": 86,
                  "bboxDp": [
                    121.091,
                    193.091,
                    124.727,
                    198.182
                  ]
                },
                {
                  "bboxPx": [
                    535,
                    551,
                    546,
                    557
                  ],
                  "pixelArea": 36,
                  "bboxDp": [
                    194.545,
                    200.364,
                    198.545,
                    202.545
                  ]
                },
                {
                  "bboxPx": [
                    70,
                    606,
                    78,
                    614
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    25.455,
                    220.364,
                    28.364,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    273,
                    606,
                    281,
                    614
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    99.273,
                    220.364,
                    102.182,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    476,
                    606,
                    484,
                    614
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    173.091,
                    220.364,
                    176.0,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    678,
                    606,
                    686,
                    614
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    246.545,
                    220.364,
                    249.455,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    881,
                    606,
                    889,
                    614
                  ],
                  "pixelArea": 60,
                  "bboxDp": [
                    320.364,
                    220.364,
                    323.273,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    397,
                    607,
                    408,
                    613
                  ],
                  "pixelArea": 66,
                  "bboxDp": [
                    144.364,
                    220.727,
                    148.364,
                    222.909
                  ]
                },
                {
                  "bboxPx": [
                    793,
                    607,
                    808,
                    614
                  ],
                  "pixelArea": 105,
                  "bboxDp": [
                    288.364,
                    220.727,
                    293.818,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    764,
                    609,
                    774,
                    614
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    277.818,
                    221.455,
                    281.455,
                    223.273
                  ]
                },
                {
                  "bboxPx": [
                    553,
                    610,
                    565,
                    616
                  ],
                  "pixelArea": 59,
                  "bboxDp": [
                    201.091,
                    221.818,
                    205.455,
                    224.0
                  ]
                },
                {
                  "bboxPx": [
                    181,
                    615,
                    198,
                    632
                  ],
                  "pixelArea": 134,
                  "bboxDp": [
                    65.818,
                    223.636,
                    72.0,
                    229.818
                  ]
                },
                {
                  "bboxPx": [
                    595,
                    620,
                    606,
                    628
                  ],
                  "pixelArea": 57,
                  "bboxDp": [
                    216.364,
                    225.455,
                    220.364,
                    228.364
                  ]
                },
                {
                  "bboxPx": [
                    806,
                    625,
                    814,
                    633
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    293.091,
                    227.273,
                    296.0,
                    230.182
                  ]
                },
                {
                  "bboxPx": [
                    153,
                    627,
                    165,
                    634
                  ],
                  "pixelArea": 84,
                  "bboxDp": [
                    55.636,
                    228.0,
                    60.0,
                    230.545
                  ]
                }
              ],
              "totalQualifyingRegions": 25
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    939,
                    2198,
                    956,
                    2212
                  ],
                  "pixelArea": 153,
                  "bboxDp": [
                    341.455,
                    799.273,
                    347.636,
                    804.364
                  ]
                },
                {
                  "bboxPx": [
                    924,
                    2228,
                    974,
                    2249
                  ],
                  "pixelArea": 600,
                  "bboxDp": [
                    336.0,
                    810.182,
                    354.182,
                    817.818
                  ]
                },
                {
                  "bboxPx": [
                    917,
                    2273,
                    944,
                    2301
                  ],
                  "pixelArea": 301,
                  "bboxDp": [
                    333.455,
                    826.545,
                    343.273,
                    836.727
                  ]
                },
                {
                  "bboxPx": [
                    948,
                    2273,
                    973,
                    2301
                  ],
                  "pixelArea": 249,
                  "bboxDp": [
                    344.727,
                    826.545,
                    353.818,
                    836.727
                  ]
                }
              ],
              "totalQualifyingRegions": 4
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                944,
                177,
                1009,
                242
              ],
              "bboxDp": [
                343.273,
                64.364,
                366.909,
                88.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                812,
                181,
                877,
                241
              ],
              "bboxDp": [
                295.273,
                65.818,
                318.909,
                87.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                962,
                195,
                991,
                224
              ],
              "bboxDp": [
                349.818,
                70.909,
                360.364,
                81.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                242,
                319,
                401,
                370
              ],
              "bboxDp": [
                88.0,
                116.0,
                145.818,
                134.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                241,
                387,
                402,
                418
              ],
              "bboxDp": [
                87.636,
                140.727,
                146.182,
                152.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                712,
                505,
                772,
                565
              ],
              "bboxDp": [
                258.909,
                183.636,
                280.727,
                205.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                919,
                508,
                971,
                568
              ],
              "bboxDp": [
                334.182,
                184.727,
                353.091,
                206.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                107,
                511,
                159,
                569
              ],
              "bboxDp": [
                38.909,
                185.818,
                57.818,
                206.909
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                305,
                511,
                369,
                565
              ],
              "bboxDp": [
                110.909,
                185.818,
                134.182,
                205.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                507,
                511,
                573,
                562
              ],
              "bboxDp": [
                184.364,
                185.818,
                208.364,
                204.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                327,
                525,
                350,
                551
              ],
              "bboxDp": [
                118.909,
                190.909,
                127.273,
                200.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                58,
                602,
                210,
                639
              ],
              "bboxDp": [
                21.091,
                218.909,
                76.364,
                232.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                261,
                602,
                413,
                639
              ],
              "bboxDp": [
                94.909,
                218.909,
                150.182,
                232.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                464,
                602,
                616,
                639
              ],
              "bboxDp": [
                168.727,
                218.909,
                224.0,
                232.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                666,
                602,
                817,
                639
              ],
              "bboxDp": [
                242.182,
                218.909,
                297.091,
                232.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                869,
                602,
                1021,
                639
              ],
              "bboxDp": [
                316.0,
                218.909,
                371.273,
                232.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                787,
                751,
                1009,
                857
              ],
              "bboxDp": [
                286.182,
                273.091,
                366.909,
                311.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1163,
                211,
                1208
              ],
              "bboxDp": [
                26.909,
                422.909,
                76.727,
                439.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                595,
                1163,
                873,
                1208
              ],
              "bboxDp": [
                216.364,
                422.909,
                317.455,
                439.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                595,
                1414,
                779,
                1459
              ],
              "bboxDp": [
                216.364,
                514.182,
                283.273,
                530.545
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                177,
                1685,
                351,
                1727
              ],
              "bboxDp": [
                64.364,
                612.727,
                127.636,
                628.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                177,
                1894,
                351,
                1936
              ],
              "bboxDp": [
                64.364,
                688.727,
                127.636,
                704.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                178,
                2103,
                394,
                2145
              ],
              "bboxDp": [
                64.727,
                764.727,
                143.273,
                780.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        },
        "reference": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    904,
                    318,
                    1003,
                    417
                  ],
                  "pixelArea": 5249,
                  "bboxDp": [
                    328.727,
                    115.636,
                    364.727,
                    151.636
                  ]
                },
                {
                  "bboxPx": [
                    128,
                    333,
                    153,
                    357
                  ],
                  "pixelArea": 207,
                  "bboxDp": [
                    46.545,
                    121.091,
                    55.636,
                    129.818
                  ]
                },
                {
                  "bboxPx": [
                    128,
                    355,
                    147,
                    364
                  ],
                  "pixelArea": 78,
                  "bboxDp": [
                    46.545,
                    129.091,
                    53.455,
                    132.364
                  ]
                },
                {
                  "bboxPx": [
                    112,
                    373,
                    121,
                    381
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    40.727,
                    135.636,
                    44.0,
                    138.545
                  ]
                },
                {
                  "bboxPx": [
                    125,
                    373,
                    169,
                    397
                  ],
                  "pixelArea": 717,
                  "bboxDp": [
                    45.455,
                    135.636,
                    61.455,
                    144.364
                  ]
                },
                {
                  "bboxPx": [
                    745,
                    526,
                    765,
                    533
                  ],
                  "pixelArea": 122,
                  "bboxDp": [
                    270.909,
                    191.273,
                    278.182,
                    193.818
                  ]
                },
                {
                  "bboxPx": [
                    722,
                    540,
                    762,
                    562
                  ],
                  "pixelArea": 774,
                  "bboxDp": [
                    262.545,
                    196.364,
                    277.091,
                    204.364
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1105,
                    526,
                    1578
                  ],
                  "pixelArea": 225638,
                  "bboxDp": [
                    12.0,
                    401.818,
                    191.273,
                    573.818
                  ]
                },
                {
                  "bboxPx": [
                    554,
                    1105,
                    1047,
                    1328
                  ],
                  "pixelArea": 92045,
                  "bboxDp": [
                    201.455,
                    401.818,
                    380.727,
                    482.909
                  ]
                },
                {
                  "bboxPx": [
                    804,
                    1174,
                    813,
                    1185
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    292.364,
                    426.909,
                    295.636,
                    430.909
                  ]
                },
                {
                  "bboxPx": [
                    742,
                    1175,
                    754,
                    1182
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    269.818,
                    427.273,
                    274.182,
                    429.818
                  ]
                },
                {
                  "bboxPx": [
                    706,
                    1177,
                    715,
                    1183
                  ],
                  "pixelArea": 23,
                  "bboxDp": [
                    256.727,
                    428.0,
                    260.0,
                    430.182
                  ]
                },
                {
                  "bboxPx": [
                    650,
                    1182,
                    667,
                    1199
                  ],
                  "pixelArea": 108,
                  "bboxDp": [
                    236.364,
                    429.818,
                    242.545,
                    436.0
                  ]
                },
                {
                  "bboxPx": [
                    178,
                    1184,
                    186,
                    1205
                  ],
                  "pixelArea": 88,
                  "bboxDp": [
                    64.727,
                    430.545,
                    67.636,
                    438.182
                  ]
                },
                {
                  "bboxPx": [
                    618,
                    1185,
                    627,
                    1194
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    224.727,
                    430.909,
                    228.0,
                    434.182
                  ]
                },
                {
                  "bboxPx": [
                    696,
                    1186,
                    704,
                    1193
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    253.091,
                    431.273,
                    256.0,
                    433.818
                  ]
                },
                {
                  "bboxPx": [
                    741,
                    1188,
                    754,
                    1202
                  ],
                  "pixelArea": 43,
                  "bboxDp": [
                    269.455,
                    432.0,
                    274.182,
                    437.091
                  ]
                },
                {
                  "bboxPx": [
                    188,
                    1191,
                    200,
                    1202
                  ],
                  "pixelArea": 59,
                  "bboxDp": [
                    68.364,
                    433.091,
                    72.727,
                    437.091
                  ]
                },
                {
                  "bboxPx": [
                    707,
                    1196,
                    715,
                    1204
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    257.091,
                    434.909,
                    260.0,
                    437.818
                  ]
                },
                {
                  "bboxPx": [
                    959,
                    1209,
                    992,
                    1226
                  ],
                  "pixelArea": 132,
                  "bboxDp": [
                    348.727,
                    439.636,
                    360.727,
                    445.818
                  ]
                },
                {
                  "bboxPx": [
                    621,
                    1262,
                    630,
                    1284
                  ],
                  "pixelArea": 170,
                  "bboxDp": [
                    225.818,
                    458.909,
                    229.091,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    652,
                    1262,
                    661,
                    1284
                  ],
                  "pixelArea": 170,
                  "bboxDp": [
                    237.091,
                    458.909,
                    240.364,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    759,
                    1262,
                    768,
                    1284
                  ],
                  "pixelArea": 170,
                  "bboxDp": [
                    276.0,
                    458.909,
                    279.273,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    554,
                    1356,
                    1047,
                    1579
                  ],
                  "pixelArea": 94759,
                  "bboxDp": [
                    201.455,
                    493.091,
                    380.727,
                    574.182
                  ]
                },
                {
                  "bboxPx": [
                    966,
                    1445,
                    985,
                    1464
                  ],
                  "pixelArea": 200,
                  "bboxDp": [
                    351.273,
                    525.455,
                    358.182,
                    532.364
                  ]
                },
                {
                  "bboxPx": [
                    618,
                    1448,
                    630,
                    1454
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    224.727,
                    526.545,
                    229.091,
                    528.727
                  ]
                },
                {
                  "bboxPx": [
                    728,
                    1515,
                    736,
                    1524
                  ],
                  "pixelArea": 52,
                  "bboxDp": [
                    264.727,
                    550.909,
                    267.636,
                    554.182
                  ]
                },
                {
                  "bboxPx": [
                    800,
                    1516,
                    811,
                    1526
                  ],
                  "pixelArea": 97,
                  "bboxDp": [
                    290.909,
                    551.273,
                    294.909,
                    554.909
                  ]
                },
                {
                  "bboxPx": [
                    755,
                    1520,
                    766,
                    1532
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    274.545,
                    552.727,
                    278.545,
                    557.091
                  ]
                },
                {
                  "bboxPx": [
                    685,
                    1525,
                    696,
                    1533
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    249.091,
                    554.545,
                    253.091,
                    557.455
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1611,
                    1047,
                    1798
                  ],
                  "pixelArea": 178960,
                  "bboxDp": [
                    12.0,
                    585.818,
                    380.727,
                    653.818
                  ]
                },
                {
                  "bboxPx": [
                    109,
                    1691,
                    122,
                    1713
                  ],
                  "pixelArea": 121,
                  "bboxDp": [
                    39.636,
                    614.909,
                    44.364,
                    622.909
                  ]
                },
                {
                  "bboxPx": [
                    191,
                    1694,
                    203,
                    1704
                  ],
                  "pixelArea": 101,
                  "bboxDp": [
                    69.455,
                    616.0,
                    73.818,
                    619.636
                  ]
                },
                {
                  "bboxPx": [
                    275,
                    1700,
                    284,
                    1708
                  ],
                  "pixelArea": 71,
                  "bboxDp": [
                    100.0,
                    618.182,
                    103.273,
                    621.091
                  ]
                },
                {
                  "bboxPx": [
                    291,
                    1700,
                    299,
                    1708
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    105.818,
                    618.182,
                    108.727,
                    621.091
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    1831,
                    1047,
                    2018
                  ],
                  "pixelArea": 178029,
                  "bboxDp": [
                    12.0,
                    665.818,
                    380.727,
                    733.818
                  ]
                },
                {
                  "bboxPx": [
                    109,
                    1935,
                    120,
                    1943
                  ],
                  "pixelArea": 43,
                  "bboxDp": [
                    39.636,
                    703.636,
                    43.636,
                    706.545
                  ]
                },
                {
                  "bboxPx": [
                    33,
                    2051,
                    1047,
                    2151
                  ],
                  "pixelArea": 93687,
                  "bboxDp": [
                    12.0,
                    745.818,
                    380.727,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    2133,
                    121,
                    2151
                  ],
                  "pixelArea": 193,
                  "bboxDp": [
                    34.909,
                    775.636,
                    44.0,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    247,
                    2138,
                    257,
                    2151
                  ],
                  "pixelArea": 87,
                  "bboxDp": [
                    89.818,
                    777.455,
                    93.455,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    2139,
                    334,
                    2149
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    116.727,
                    777.818,
                    121.455,
                    781.455
                  ]
                },
                {
                  "bboxPx": [
                    280,
                    2141,
                    292,
                    2147
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    101.818,
                    778.545,
                    106.182,
                    780.727
                  ]
                },
                {
                  "bboxPx": [
                    194,
                    2144,
                    202,
                    2151
                  ],
                  "pixelArea": 29,
                  "bboxDp": [
                    70.545,
                    779.636,
                    73.455,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    0,
                    2152,
                    1080,
                    2334
                  ],
                  "pixelArea": 179897,
                  "bboxDp": [
                    0.0,
                    782.545,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    118,
                    2203,
                    152,
                    2237
                  ],
                  "pixelArea": 854,
                  "bboxDp": [
                    42.909,
                    801.091,
                    55.273,
                    813.455
                  ]
                },
                {
                  "bboxPx": [
                    674,
                    2208,
                    687,
                    2221
                  ],
                  "pixelArea": 49,
                  "bboxDp": [
                    245.091,
                    802.909,
                    249.818,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    659,
                    2274,
                    669,
                    2282
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    239.636,
                    826.909,
                    243.273,
                    829.818
                  ]
                },
                {
                  "bboxPx": [
                    412,
                    2280,
                    423,
                    2289
                  ],
                  "pixelArea": 45,
                  "bboxDp": [
                    149.818,
                    829.091,
                    153.818,
                    832.364
                  ]
                }
              ],
              "totalQualifyingRegions": 48
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    2151
                  ],
                  "pixelArea": 875432,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    782.182
                  ]
                },
                {
                  "bboxPx": [
                    948,
                    181,
                    1003,
                    236
                  ],
                  "pixelArea": 2008,
                  "bboxDp": [
                    344.727,
                    65.818,
                    364.727,
                    85.818
                  ]
                },
                {
                  "bboxPx": [
                    816,
                    185,
                    871,
                    234
                  ],
                  "pixelArea": 2114,
                  "bboxDp": [
                    296.727,
                    67.273,
                    316.727,
                    85.091
                  ]
                },
                {
                  "bboxPx": [
                    310,
                    351,
                    332,
                    357
                  ],
                  "pixelArea": 132,
                  "bboxDp": [
                    112.727,
                    127.636,
                    120.727,
                    129.818
                  ]
                },
                {
                  "bboxPx": [
                    314,
                    361,
                    329,
                    369
                  ],
                  "pixelArea": 93,
                  "bboxDp": [
                    114.182,
                    131.273,
                    119.636,
                    134.182
                  ]
                },
                {
                  "bboxPx": [
                    924,
                    516,
                    966,
                    564
                  ],
                  "pixelArea": 1447,
                  "bboxDp": [
                    336.0,
                    187.636,
                    351.273,
                    205.091
                  ]
                },
                {
                  "bboxPx": [
                    113,
                    518,
                    135,
                    536
                  ],
                  "pixelArea": 394,
                  "bboxDp": [
                    41.091,
                    188.364,
                    49.091,
                    194.909
                  ]
                },
                {
                  "bboxPx": [
                    310,
                    518,
                    364,
                    562
                  ],
                  "pixelArea": 1795,
                  "bboxDp": [
                    112.727,
                    188.364,
                    132.364,
                    204.364
                  ]
                },
                {
                  "bboxPx": [
                    519,
                    522,
                    561,
                    544
                  ],
                  "pixelArea": 761,
                  "bboxDp": [
                    188.727,
                    189.818,
                    204.0,
                    197.818
                  ]
                },
                {
                  "bboxPx": [
                    132,
                    530,
                    152,
                    548
                  ],
                  "pixelArea": 213,
                  "bboxDp": [
                    48.0,
                    192.727,
                    55.273,
                    199.273
                  ]
                },
                {
                  "bboxPx": [
                    333,
                    533,
                    343,
                    547
                  ],
                  "pixelArea": 85,
                  "bboxDp": [
                    121.091,
                    193.818,
                    124.727,
                    198.909
                  ]
                },
                {
                  "bboxPx": [
                    396,
                    621,
                    407,
                    627
                  ],
                  "pixelArea": 66,
                  "bboxDp": [
                    144.0,
                    225.818,
                    148.0,
                    228.0
                  ]
                },
                {
                  "bboxPx": [
                    792,
                    621,
                    806,
                    628
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    288.0,
                    225.818,
                    293.091,
                    228.364
                  ]
                },
                {
                  "bboxPx": [
                    763,
                    623,
                    773,
                    628
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    277.455,
                    226.545,
                    281.091,
                    228.364
                  ]
                },
                {
                  "bboxPx": [
                    553,
                    624,
                    565,
                    630
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    201.091,
                    226.909,
                    205.455,
                    229.091
                  ]
                },
                {
                  "bboxPx": [
                    305,
                    626,
                    313,
                    633
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    110.909,
                    227.636,
                    113.818,
                    230.182
                  ]
                },
                {
                  "bboxPx": [
                    710,
                    626,
                    718,
                    633
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    258.182,
                    227.636,
                    261.091,
                    230.182
                  ]
                },
                {
                  "bboxPx": [
                    180,
                    629,
                    196,
                    646
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    65.455,
                    228.727,
                    71.273,
                    234.909
                  ]
                },
                {
                  "bboxPx": [
                    594,
                    634,
                    605,
                    642
                  ],
                  "pixelArea": 55,
                  "bboxDp": [
                    216.0,
                    230.545,
                    220.0,
                    233.455
                  ]
                },
                {
                  "bboxPx": [
                    305,
                    637,
                    313,
                    645
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    110.909,
                    231.636,
                    113.818,
                    234.545
                  ]
                },
                {
                  "bboxPx": [
                    710,
                    637,
                    718,
                    645
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    258.182,
                    231.636,
                    261.091,
                    234.545
                  ]
                },
                {
                  "bboxPx": [
                    804,
                    639,
                    812,
                    647
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    292.364,
                    232.364,
                    295.273,
                    235.273
                  ]
                },
                {
                  "bboxPx": [
                    153,
                    641,
                    165,
                    647
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    55.636,
                    233.091,
                    60.0,
                    235.273
                  ]
                }
              ],
              "totalQualifyingRegions": 23
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    939,
                    2199,
                    954,
                    2213
                  ],
                  "pixelArea": 124,
                  "bboxDp": [
                    341.455,
                    799.636,
                    346.909,
                    804.727
                  ]
                },
                {
                  "bboxPx": [
                    925,
                    2229,
                    973,
                    2249
                  ],
                  "pixelArea": 533,
                  "bboxDp": [
                    336.364,
                    810.545,
                    353.818,
                    817.818
                  ]
                },
                {
                  "bboxPx": [
                    917,
                    2269,
                    944,
                    2297
                  ],
                  "pixelArea": 307,
                  "bboxDp": [
                    333.455,
                    825.091,
                    343.273,
                    835.273
                  ]
                },
                {
                  "bboxPx": [
                    948,
                    2269,
                    973,
                    2297
                  ],
                  "pixelArea": 241,
                  "bboxDp": [
                    344.727,
                    825.091,
                    353.818,
                    835.273
                  ]
                }
              ],
              "totalQualifyingRegions": 4
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                943,
                176,
                1008,
                241
              ],
              "bboxDp": [
                342.909,
                64.0,
                366.545,
                87.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                811,
                180,
                876,
                240
              ],
              "bboxDp": [
                294.909,
                65.455,
                318.545,
                87.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                961,
                194,
                990,
                223
              ],
              "bboxDp": [
                349.455,
                70.545,
                360.0,
                81.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                242,
                323,
                401,
                374
              ],
              "bboxDp": [
                88.0,
                117.455,
                145.818,
                136.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                241,
                390,
                402,
                421
              ],
              "bboxDp": [
                87.636,
                141.818,
                146.182,
                153.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                712,
                507,
                772,
                567
              ],
              "bboxDp": [
                258.909,
                184.364,
                280.727,
                206.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                919,
                510,
                971,
                570
              ],
              "bboxDp": [
                334.182,
                185.455,
                353.091,
                207.273
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                107,
                513,
                159,
                571
              ],
              "bboxDp": [
                38.909,
                186.545,
                57.818,
                207.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                305,
                513,
                369,
                567
              ],
              "bboxDp": [
                110.909,
                186.545,
                134.182,
                206.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                508,
                517,
                572,
                561
              ],
              "bboxDp": [
                184.727,
                188.0,
                208.0,
                204.0
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                327,
                527,
                350,
                553
              ],
              "bboxDp": [
                118.909,
                191.636,
                127.273,
                201.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                60,
                616,
                208,
                653
              ],
              "bboxDp": [
                21.818,
                224.0,
                75.636,
                237.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                263,
                616,
                412,
                653
              ],
              "bboxDp": [
                95.636,
                224.0,
                149.818,
                237.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                466,
                616,
                614,
                653
              ],
              "bboxDp": [
                169.455,
                224.0,
                223.273,
                237.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                668,
                616,
                815,
                653
              ],
              "bboxDp": [
                242.909,
                224.0,
                296.364,
                237.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                871,
                616,
                1019,
                653
              ],
              "bboxDp": [
                316.727,
                224.0,
                370.545,
                237.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                789,
                750,
                1009,
                855
              ],
              "bboxDp": [
                286.909,
                272.727,
                366.909,
                310.909
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1166,
                209,
                1211
              ],
              "bboxDp": [
                26.909,
                424.0,
                76.0,
                440.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                595,
                1166,
                868,
                1211
              ],
              "bboxDp": [
                216.364,
                424.0,
                315.636,
                440.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                595,
                1417,
                776,
                1462
              ],
              "bboxDp": [
                216.364,
                515.273,
                282.182,
                531.636
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                178,
                1686,
                352,
                1728
              ],
              "bboxDp": [
                64.727,
                613.091,
                128.0,
                628.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                178,
                1906,
                352,
                1948
              ],
              "bboxDp": [
                64.727,
                693.091,
                128.0,
                708.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                179,
                2126,
                395,
                2151
              ],
              "bboxDp": [
                65.091,
                773.091,
                143.636,
                782.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        }
      },
      "inkPositionCandidates": [
        {
          "originalRegion": 0,
          "referenceRegion": 0,
          "originalBboxPx": [
            944,
            177,
            1009,
            242
          ],
          "referenceBboxPx": [
            943,
            176,
            1008,
            241
          ],
          "edgeDeltaPx": [
            -1,
            -1,
            -1,
            -1
          ],
          "edgeDeltaDp": [
            -0.364,
            -0.364,
            -0.364,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 1,
          "referenceRegion": 1,
          "originalBboxPx": [
            812,
            181,
            877,
            241
          ],
          "referenceBboxPx": [
            811,
            180,
            876,
            240
          ],
          "edgeDeltaPx": [
            -1,
            -1,
            -1,
            -1
          ],
          "edgeDeltaDp": [
            -0.364,
            -0.364,
            -0.364,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 2,
          "referenceRegion": 2,
          "originalBboxPx": [
            962,
            195,
            991,
            224
          ],
          "referenceBboxPx": [
            961,
            194,
            990,
            223
          ],
          "edgeDeltaPx": [
            -1,
            -1,
            -1,
            -1
          ],
          "edgeDeltaDp": [
            -0.364,
            -0.364,
            -0.364,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 3,
          "referenceRegion": 3,
          "originalBboxPx": [
            242,
            319,
            401,
            370
          ],
          "referenceBboxPx": [
            242,
            323,
            401,
            374
          ],
          "edgeDeltaPx": [
            0,
            4,
            0,
            4
          ],
          "edgeDeltaDp": [
            0.0,
            1.455,
            0.0,
            1.455
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 4,
          "referenceRegion": 4,
          "originalBboxPx": [
            241,
            387,
            402,
            418
          ],
          "referenceBboxPx": [
            241,
            390,
            402,
            421
          ],
          "edgeDeltaPx": [
            0,
            3,
            0,
            3
          ],
          "edgeDeltaDp": [
            0.0,
            1.091,
            0.0,
            1.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 5,
          "referenceRegion": 5,
          "originalBboxPx": [
            712,
            505,
            772,
            565
          ],
          "referenceBboxPx": [
            712,
            507,
            772,
            567
          ],
          "edgeDeltaPx": [
            0,
            2,
            0,
            2
          ],
          "edgeDeltaDp": [
            0.0,
            0.727,
            0.0,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 6,
          "referenceRegion": 6,
          "originalBboxPx": [
            919,
            508,
            971,
            568
          ],
          "referenceBboxPx": [
            919,
            510,
            971,
            570
          ],
          "edgeDeltaPx": [
            0,
            2,
            0,
            2
          ],
          "edgeDeltaDp": [
            0.0,
            0.727,
            0.0,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 7,
          "referenceRegion": 7,
          "originalBboxPx": [
            107,
            511,
            159,
            569
          ],
          "referenceBboxPx": [
            107,
            513,
            159,
            571
          ],
          "edgeDeltaPx": [
            0,
            2,
            0,
            2
          ],
          "edgeDeltaDp": [
            0.0,
            0.727,
            0.0,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 8,
          "referenceRegion": 8,
          "originalBboxPx": [
            305,
            511,
            369,
            565
          ],
          "referenceBboxPx": [
            305,
            513,
            369,
            567
          ],
          "edgeDeltaPx": [
            0,
            2,
            0,
            2
          ],
          "edgeDeltaDp": [
            0.0,
            0.727,
            0.0,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 9,
          "referenceRegion": 9,
          "originalBboxPx": [
            507,
            511,
            573,
            562
          ],
          "referenceBboxPx": [
            508,
            517,
            572,
            561
          ],
          "edgeDeltaPx": [
            1,
            6,
            -1,
            -1
          ],
          "edgeDeltaDp": [
            0.364,
            2.182,
            -0.364,
            -0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 10,
          "referenceRegion": 10,
          "originalBboxPx": [
            327,
            525,
            350,
            551
          ],
          "referenceBboxPx": [
            327,
            527,
            350,
            553
          ],
          "edgeDeltaPx": [
            0,
            2,
            0,
            2
          ],
          "edgeDeltaDp": [
            0.0,
            0.727,
            0.0,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 11,
          "referenceRegion": 11,
          "originalBboxPx": [
            58,
            602,
            210,
            639
          ],
          "referenceBboxPx": [
            60,
            616,
            208,
            653
          ],
          "edgeDeltaPx": [
            2,
            14,
            -2,
            14
          ],
          "edgeDeltaDp": [
            0.727,
            5.091,
            -0.727,
            5.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 12,
          "referenceRegion": 12,
          "originalBboxPx": [
            261,
            602,
            413,
            639
          ],
          "referenceBboxPx": [
            263,
            616,
            412,
            653
          ],
          "edgeDeltaPx": [
            2,
            14,
            -1,
            14
          ],
          "edgeDeltaDp": [
            0.727,
            5.091,
            -0.364,
            5.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 13,
          "referenceRegion": 13,
          "originalBboxPx": [
            464,
            602,
            616,
            639
          ],
          "referenceBboxPx": [
            466,
            616,
            614,
            653
          ],
          "edgeDeltaPx": [
            2,
            14,
            -2,
            14
          ],
          "edgeDeltaDp": [
            0.727,
            5.091,
            -0.727,
            5.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 14,
          "referenceRegion": 14,
          "originalBboxPx": [
            666,
            602,
            817,
            639
          ],
          "referenceBboxPx": [
            668,
            616,
            815,
            653
          ],
          "edgeDeltaPx": [
            2,
            14,
            -2,
            14
          ],
          "edgeDeltaDp": [
            0.727,
            5.091,
            -0.727,
            5.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 15,
          "referenceRegion": 15,
          "originalBboxPx": [
            869,
            602,
            1021,
            639
          ],
          "referenceBboxPx": [
            871,
            616,
            1019,
            653
          ],
          "edgeDeltaPx": [
            2,
            14,
            -2,
            14
          ],
          "edgeDeltaDp": [
            0.727,
            5.091,
            -0.727,
            5.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 16,
          "referenceRegion": 16,
          "originalBboxPx": [
            787,
            751,
            1009,
            857
          ],
          "referenceBboxPx": [
            789,
            750,
            1009,
            855
          ],
          "edgeDeltaPx": [
            2,
            -1,
            0,
            -2
          ],
          "edgeDeltaDp": [
            0.727,
            -0.364,
            0.0,
            -0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 17,
          "referenceRegion": 17,
          "originalBboxPx": [
            74,
            1163,
            211,
            1208
          ],
          "referenceBboxPx": [
            74,
            1166,
            209,
            1211
          ],
          "edgeDeltaPx": [
            0,
            3,
            -2,
            3
          ],
          "edgeDeltaDp": [
            0.0,
            1.091,
            -0.727,
            1.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 18,
          "referenceRegion": 18,
          "originalBboxPx": [
            595,
            1163,
            873,
            1208
          ],
          "referenceBboxPx": [
            595,
            1166,
            868,
            1211
          ],
          "edgeDeltaPx": [
            0,
            3,
            -5,
            3
          ],
          "edgeDeltaDp": [
            0.0,
            1.091,
            -1.818,
            1.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 19,
          "referenceRegion": 19,
          "originalBboxPx": [
            595,
            1414,
            779,
            1459
          ],
          "referenceBboxPx": [
            595,
            1417,
            776,
            1462
          ],
          "edgeDeltaPx": [
            0,
            3,
            -3,
            3
          ],
          "edgeDeltaDp": [
            0.0,
            1.091,
            -1.091,
            1.091
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 20,
          "referenceRegion": 20,
          "originalBboxPx": [
            177,
            1685,
            351,
            1727
          ],
          "referenceBboxPx": [
            178,
            1686,
            352,
            1728
          ],
          "edgeDeltaPx": [
            1,
            1,
            1,
            1
          ],
          "edgeDeltaDp": [
            0.364,
            0.364,
            0.364,
            0.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 21,
          "referenceRegion": 21,
          "originalBboxPx": [
            177,
            1894,
            351,
            1936
          ],
          "referenceBboxPx": [
            178,
            1906,
            352,
            1948
          ],
          "edgeDeltaPx": [
            1,
            12,
            1,
            12
          ],
          "edgeDeltaDp": [
            0.364,
            4.364,
            0.364,
            4.364
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 22,
          "referenceRegion": 22,
          "originalBboxPx": [
            178,
            2103,
            394,
            2145
          ],
          "referenceBboxPx": [
            179,
            2126,
            395,
            2151
          ],
          "edgeDeltaPx": [
            1,
            23,
            1,
            6
          ],
          "edgeDeltaDp": [
            0.364,
            8.364,
            0.364,
            2.182
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        }
      ],
      "manualReviewInputMatches": true,
      "components": [
        {
          "id": "ProfileHeader",
          "page": "mine-day",
          "cropBoxPx": [
            60,
            288,
            1025,
            445
          ],
          "sourceBoundsPx": [
            69,
            296,
            1011,
            434
          ],
          "basis": "Visible 50dp avatar [69,296,206,434], name [239,306,404,380], description [239,380,404,424], trailing badge",
          "notes": "Logged-out five-shortcut runtime baseline mine 11.202.21; glyph boxes differ from text view boxes.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "c8857401ecf63646478bfd7b522b243bf112ed10fb80ee238e9bc11e7f067348",
            "reference": "315bfedfdec46529b86bf1d7fc188d881a68c07b99211ac56eade4d4c7125ab6"
          },
          "originalEvidenceId": "ev_1d57d0e878ed7ab61a9f841d5766bacd63d04fe53bc3af5fed5b36a9420e86d8",
          "referenceEvidenceId": "ev_6169a1dfb3c6ff777e10df791be209bc1dd23343dbb21f6191f0763f7feae694",
          "images": {
            "sideBySide": {
              "path": "generated/component-ProfileHeader-side-by-side.png",
              "sha256": "932cf7b1c11fc4060b93d415e9e2bc54d76377cae343148e61c953e6f092e39e",
              "canvasPx": [
                1930,
                157
              ]
            },
            "overlay": {
              "path": "generated/component-ProfileHeader-overlay.png",
              "sha256": "a43085492a9c1d8101c09081efa0015c4066981e6162b6228337aa679fb7711c",
              "canvasPx": [
                965,
                157
              ]
            },
            "difference": {
              "path": "generated/component-ProfileHeader-difference-x3.png",
              "sha256": "f4de126768458b994d30c1682b8ce63244505d1e11c83edc696cd592e18d3f59",
              "canvasPx": [
                965,
                157
              ]
            }
          }
        },
        {
          "id": "ShortcutGrid",
          "page": "mine-day",
          "cropBoxPx": [
            25,
            497,
            1055,
            681
          ],
          "sourceBoundsPx": [
            33,
            505,
            1047,
            673
          ],
          "basis": "spec.md five ordered equal-width entrances and original icon/label region with 8px context",
          "notes": "Preserves Activity/Courses/Friends/Prizes/Orders order. Does not validate more-item overflow.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "c8857401ecf63646478bfd7b522b243bf112ed10fb80ee238e9bc11e7f067348",
            "reference": "315bfedfdec46529b86bf1d7fc188d881a68c07b99211ac56eade4d4c7125ab6"
          },
          "originalEvidenceId": "ev_1d57d0e878ed7ab61a9f841d5766bacd63d04fe53bc3af5fed5b36a9420e86d8",
          "referenceEvidenceId": "ev_6169a1dfb3c6ff777e10df791be209bc1dd23343dbb21f6191f0763f7feae694",
          "images": {
            "sideBySide": {
              "path": "generated/component-ShortcutGrid-side-by-side.png",
              "sha256": "550e47f6fe8f8473c026a7ca8cc3d8e501d4207d656ff094cb3d38ce9f7eec01",
              "canvasPx": [
                2060,
                184
              ]
            },
            "overlay": {
              "path": "generated/component-ShortcutGrid-overlay.png",
              "sha256": "abe248a02a9bbbbd11d169b69a04a7a89059bb45d4dbbc635a517241538df70b",
              "canvasPx": [
                1030,
                184
              ]
            },
            "difference": {
              "path": "generated/component-ShortcutGrid-difference-x3.png",
              "sha256": "1f05d217e12a9ca8b50e35ae52e4d217699f557988ce3ba995c569d27cff59b9",
              "canvasPx": [
                1030,
                184
              ]
            }
          }
        },
        {
          "id": "SurfaceCard",
          "page": "mine-day",
          "cropBoxPx": [
            25,
            1097,
            534,
            1587
          ],
          "sourceBoundsPx": [
            33,
            1105,
            526,
            1579
          ],
          "basis": "Exact white connected surface of left habit card, with 8px context",
          "notes": "RN 18dp surface variant; ordinary native card and adaptive button radii are separate roles.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "c8857401ecf63646478bfd7b522b243bf112ed10fb80ee238e9bc11e7f067348",
            "reference": "315bfedfdec46529b86bf1d7fc188d881a68c07b99211ac56eade4d4c7125ab6"
          },
          "originalEvidenceId": "ev_1d57d0e878ed7ab61a9f841d5766bacd63d04fe53bc3af5fed5b36a9420e86d8",
          "referenceEvidenceId": "ev_6169a1dfb3c6ff777e10df791be209bc1dd23343dbb21f6191f0763f7feae694",
          "images": {
            "sideBySide": {
              "path": "generated/component-SurfaceCard-side-by-side.png",
              "sha256": "b9f029ec7dde4a9e7c244ea4f8f046dc53a4d35210c33b44e14b53836799464c",
              "canvasPx": [
                1018,
                490
              ]
            },
            "overlay": {
              "path": "generated/component-SurfaceCard-overlay.png",
              "sha256": "5725d77cf6cd97f49b88503329e30432296eed18cb820de4010918c263695eae",
              "canvasPx": [
                509,
                490
              ]
            },
            "difference": {
              "path": "generated/component-SurfaceCard-difference-x3.png",
              "sha256": "13c9f36705c29ff52346705ab8141fa8e324246a591f8dc7c20e8d0ca00ed91e",
              "canvasPx": [
                509,
                490
              ]
            }
          }
        }
      ]
    },
    {
      "id": "settings-day",
      "label": "应用设置",
      "sources": {
        "original": {
          "path": "../runtime/settings-day/screen.png",
          "sha256": "209f4bac18cf8c5da8d2eb25263dcad04def3b487a3de02b904bc3ef46d254e3",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        },
        "reference": {
          "path": "../reference/settings-day/screen.png",
          "sha256": "f6bc65c3fb97598581853ed2483cdf379d663b3a37d575d246026060167eec11",
          "canvasPx": [
            1080,
            2400
          ],
          "sourceMode": "RGBA",
          "pngMetadata": {
            "srgb": "0"
          }
        }
      },
      "originalEvidence": {
        "evidenceId": "ev_627d382d9998bb8838a229e02cf660f5c7aef7aef8c1871f4720c2e113943320",
        "record": "../runtime/settingscap.rea.json",
        "expectedSha256": "209f4bac18cf8c5da8d2eb25263dcad04def3b487a3de02b904bc3ef46d254e3",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "referenceEvidence": {
        "evidenceId": "ev_086cd80eceb67a3de28f9d1e949e6fdcac174bc534980b326a7eba2c007a7feb",
        "record": "../reference/settings-day.rea.json",
        "expectedSha256": "f6bc65c3fb97598581853ed2483cdf379d663b3a37d575d246026060167eec11",
        "provider": {
          "id": "adb",
          "name": "Android Debug Bridge",
          "version": "37.0.1-15733141"
        },
        "captureTimeUtc": null,
        "recordLimitations": [
          "Artifact identity is unavailable for this observation.",
          "The capture is one frame at read time; no recording or interaction occurred"
        ],
        "origin": "current"
      },
      "images": {
        "sideBySide": {
          "path": "generated/settings-day-side-by-side.png",
          "sha256": "5b54798155d8a1492b8c3550879fb5277589f661ebe6c44954c5195c6d92481d",
          "canvasPx": [
            2160,
            2400
          ]
        },
        "overlay": {
          "path": "generated/settings-day-overlay.png",
          "sha256": "f16bcea874231528e56c0b9433ec25aed1a80f39ce6af397310f590371017dd2",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "difference": {
          "path": "generated/settings-day-difference-x3.png",
          "sha256": "c3d8baebc373a73da3cef8c55285d02b16e8923df3cfae985425aafcafede403",
          "canvasPx": [
            1080,
            2400
          ]
        },
        "rawDifference": {
          "path": "generated/settings-day-difference-raw.png",
          "sha256": "81ee1fcf6bd409fab51b9092462e97cc742c1091f11d6599cc82e159b15ac9a8",
          "canvasPx": [
            1080,
            2400
          ]
        }
      },
      "overlayBlendWeight": 0.5,
      "differenceDisplayAmplification": 3,
      "knownDifferences": [
        {
          "component": "TopBar.back",
          "classification": "D versus A",
          "observation": "返回箭头A主色#333333、墨迹[78,190,141,235]；D主色#000000、[75,188,147,237]，D更大更黑。"
        },
        {
          "component": "TopBar.title",
          "classification": "D versus A",
          "observation": "“设置”墨迹A[75,332,242,412]→D[76,332,243,412]，D右移1px；MIUI展开栏折叠未在D还原。"
        },
        {
          "component": "ChevronRow",
          "classification": "D versus A",
          "observation": "地区右值有轻微横向重影；D自绘chevron较A窄细，不能由标题位置相同推断箭头/触区相同。"
        },
        {
          "component": "PrimaryButton",
          "classification": "D versus A",
          "observation": "退出标签白墨迹A[448,2176,631,2221]→D[450,2176,630,2221]，宽度/水平位置略有差。D普通圆角与A adaptive/smooth drawable不保证数学曲线等同。"
        },
        {
          "component": "SwitchRow",
          "classification": "D versus A",
          "observation": "D为本地Canvas开关；ON填色范围对应，但按压反馈、thumb阴影、过渡动画和内部可访问性实现未逐项对照。"
        }
      ],
      "matchedDetails": [
        "#FF612E按钮精确连通框A/D均[78,2132,1002,2260]；开关橙框A/D均[884,1200,1008,1269]。框和颜色相同不等于圆角曲线全同。",
        "地区标题墨迹A/D均[73,548,158,589]；云服务均[73,806,378,848]；双接授权均[73,1004,510,1046]；通知均[74,1192,288,1234]。当前默认字体下16sp加重行标题角色和位置对应。",
        "白页、约26dp内容inset、分隔、可变行结构与固定底部动作均已在当前帧核对。",
        "该原版帧开关是ON：橙轨、右侧白thumb和开启说明一致；自定义源控件checked=false不能推翻像素和语义说明。"
      ],
      "unknowns": [
        "系统通知权限、跨重启持久化与失败恢复未知。",
        "登录态真实退出、确认/失败分支未知；访客点击无变化不能推广。",
        "MIUI标题折叠时长、曲线和中断未知。",
        "截图仅覆盖默认ON；ON/OFF操作证据由独立行为记录确认。"
      ],
      "manualReviewStatus": "reviewed-with-differences",
      "regions": {
        "original": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    2334
                  ],
                  "pixelArea": 2221190,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    176,
                    339,
                    189,
                    346
                  ],
                  "pixelArea": 91,
                  "bboxDp": [
                    64.0,
                    123.273,
                    68.727,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    196,
                    339,
                    210,
                    346
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    71.273,
                    123.273,
                    76.364,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    217,
                    339,
                    231,
                    346
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    78.909,
                    123.273,
                    84.0,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    116,
                    376,
                    141,
                    392
                  ],
                  "pixelArea": 238,
                  "bboxDp": [
                    42.182,
                    136.727,
                    51.273,
                    142.545
                  ]
                },
                {
                  "bboxPx": [
                    182,
                    399,
                    223,
                    404
                  ],
                  "pixelArea": 205,
                  "bboxDp": [
                    66.182,
                    145.091,
                    81.091,
                    146.909
                  ]
                },
                {
                  "bboxPx": [
                    815,
                    555,
                    840,
                    582
                  ],
                  "pixelArea": 368,
                  "bboxDp": [
                    296.364,
                    201.818,
                    305.455,
                    211.636
                  ]
                },
                {
                  "bboxPx": [
                    125,
                    556,
                    147,
                    577
                  ],
                  "pixelArea": 198,
                  "bboxDp": [
                    45.455,
                    202.182,
                    53.455,
                    209.818
                  ]
                },
                {
                  "bboxPx": [
                    776,
                    561,
                    786,
                    570
                  ],
                  "pixelArea": 90,
                  "bboxDp": [
                    282.182,
                    204.0,
                    285.818,
                    207.273
                  ]
                },
                {
                  "bboxPx": [
                    790,
                    561,
                    801,
                    570
                  ],
                  "pixelArea": 99,
                  "bboxDp": [
                    287.273,
                    204.0,
                    291.273,
                    207.273
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    567,
                    110,
                    584
                  ],
                  "pixelArea": 100,
                  "bboxDp": [
                    34.909,
                    206.182,
                    40.0,
                    212.364
                  ]
                },
                {
                  "bboxPx": [
                    214,
                    815,
                    226,
                    821
                  ],
                  "pixelArea": 22,
                  "bboxDp": [
                    77.818,
                    296.364,
                    82.182,
                    298.545
                  ]
                },
                {
                  "bboxPx": [
                    126,
                    821,
                    142,
                    837
                  ],
                  "pixelArea": 102,
                  "bboxDp": [
                    45.818,
                    298.545,
                    51.636,
                    304.364
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    824,
                    104,
                    832
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    34.909,
                    299.636,
                    37.818,
                    302.545
                  ]
                },
                {
                  "bboxPx": [
                    180,
                    835,
                    188,
                    842
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    65.455,
                    303.636,
                    68.364,
                    306.182
                  ]
                },
                {
                  "bboxPx": [
                    353,
                    1011,
                    363,
                    1037
                  ],
                  "pixelArea": 112,
                  "bboxDp": [
                    128.364,
                    367.636,
                    132.0,
                    377.091
                  ]
                },
                {
                  "bboxPx": [
                    214,
                    1013,
                    226,
                    1019
                  ],
                  "pixelArea": 22,
                  "bboxDp": [
                    77.818,
                    368.364,
                    82.182,
                    370.545
                  ]
                },
                {
                  "bboxPx": [
                    494,
                    1013,
                    503,
                    1027
                  ],
                  "pixelArea": 83,
                  "bboxDp": [
                    179.636,
                    368.364,
                    182.909,
                    373.455
                  ]
                },
                {
                  "bboxPx": [
                    126,
                    1019,
                    142,
                    1035
                  ],
                  "pixelArea": 102,
                  "bboxDp": [
                    45.818,
                    370.545,
                    51.636,
                    376.364
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    1022,
                    104,
                    1030
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    34.909,
                    371.636,
                    37.818,
                    374.545
                  ]
                },
                {
                  "bboxPx": [
                    272,
                    1029,
                    280,
                    1034
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    98.909,
                    374.182,
                    101.818,
                    376.0
                  ]
                },
                {
                  "bboxPx": [
                    180,
                    1033,
                    188,
                    1040
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    65.455,
                    375.636,
                    68.364,
                    378.182
                  ]
                },
                {
                  "bboxPx": [
                    666,
                    1071,
                    674,
                    1076
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    242.182,
                    389.455,
                    245.091,
                    391.273
                  ]
                },
                {
                  "bboxPx": [
                    511,
                    1072,
                    521,
                    1078
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    185.818,
                    389.818,
                    189.455,
                    392.0
                  ]
                },
                {
                  "bboxPx": [
                    624,
                    1072,
                    634,
                    1095
                  ],
                  "pixelArea": 85,
                  "bboxDp": [
                    226.909,
                    389.818,
                    230.545,
                    398.182
                  ]
                },
                {
                  "bboxPx": [
                    445,
                    1077,
                    459,
                    1091
                  ],
                  "pixelArea": 89,
                  "bboxDp": [
                    161.818,
                    391.636,
                    166.909,
                    396.727
                  ]
                },
                {
                  "bboxPx": [
                    146,
                    1079,
                    154,
                    1086
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    53.091,
                    392.364,
                    56.0,
                    394.909
                  ]
                },
                {
                  "bboxPx": [
                    421,
                    1079,
                    430,
                    1087
                  ],
                  "pixelArea": 37,
                  "bboxDp": [
                    153.091,
                    392.364,
                    156.364,
                    395.273
                  ]
                },
                {
                  "bboxPx": [
                    666,
                    1079,
                    674,
                    1084
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    242.182,
                    392.364,
                    245.091,
                    394.182
                  ]
                },
                {
                  "bboxPx": [
                    510,
                    1081,
                    521,
                    1092
                  ],
                  "pixelArea": 36,
                  "bboxDp": [
                    185.455,
                    393.091,
                    189.455,
                    397.091
                  ]
                },
                {
                  "bboxPx": [
                    263,
                    1215,
                    271,
                    1225
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    95.636,
                    441.818,
                    98.545,
                    445.455
                  ]
                },
                {
                  "bboxPx": [
                    956,
                    1217,
                    991,
                    1252
                  ],
                  "pixelArea": 893,
                  "bboxDp": [
                    347.636,
                    442.545,
                    360.364,
                    455.273
                  ]
                },
                {
                  "bboxPx": [
                    222,
                    1220,
                    243,
                    1228
                  ],
                  "pixelArea": 53,
                  "bboxDp": [
                    80.727,
                    443.636,
                    88.364,
                    446.545
                  ]
                },
                {
                  "bboxPx": [
                    380,
                    1259,
                    391,
                    1264
                  ],
                  "pixelArea": 29,
                  "bboxDp": [
                    138.182,
                    457.818,
                    142.182,
                    459.636
                  ]
                },
                {
                  "bboxPx": [
                    222,
                    1260,
                    231,
                    1273
                  ],
                  "pixelArea": 73,
                  "bboxDp": [
                    80.727,
                    458.182,
                    84.0,
                    462.909
                  ]
                },
                {
                  "bboxPx": [
                    311,
                    1267,
                    319,
                    1274
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    113.091,
                    460.727,
                    116.0,
                    463.273
                  ]
                },
                {
                  "bboxPx": [
                    381,
                    1274,
                    392,
                    1280
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    138.545,
                    463.273,
                    142.545,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    153,
                    1275,
                    166,
                    1282
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    55.636,
                    463.636,
                    60.364,
                    466.182
                  ]
                },
                {
                  "bboxPx": [
                    549,
                    1275,
                    562,
                    1282
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    199.636,
                    463.636,
                    204.364,
                    466.182
                  ]
                },
                {
                  "bboxPx": [
                    228,
                    1405,
                    236,
                    1410
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    82.909,
                    510.909,
                    85.818,
                    512.727
                  ]
                },
                {
                  "bboxPx": [
                    123,
                    1447,
                    131,
                    1452
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    44.727,
                    526.182,
                    47.636,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1447,
                    331,
                    1452
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    526.182,
                    120.364,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    81,
                    1448,
                    91,
                    1471
                  ],
                  "pixelArea": 85,
                  "bboxDp": [
                    29.455,
                    526.545,
                    33.091,
                    534.909
                  ]
                },
                {
                  "bboxPx": [
                    123,
                    1455,
                    131,
                    1460
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    44.727,
                    529.091,
                    47.636,
                    530.909
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1455,
                    331,
                    1460
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    529.091,
                    120.364,
                    530.909
                  ]
                },
                {
                  "bboxPx": [
                    187,
                    1460,
                    197,
                    1465
                  ],
                  "pixelArea": 28,
                  "bboxDp": [
                    68.0,
                    530.909,
                    71.636,
                    532.727
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1463,
                    331,
                    1468
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    532.0,
                    120.364,
                    533.818
                  ]
                },
                {
                  "bboxPx": [
                    497,
                    2177,
                    535,
                    2221
                  ],
                  "pixelArea": 770,
                  "bboxDp": [
                    180.727,
                    791.636,
                    194.545,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    542,
                    2177,
                    585,
                    2199
                  ],
                  "pixelArea": 420,
                  "bboxDp": [
                    197.091,
                    791.636,
                    212.727,
                    799.636
                  ]
                },
                {
                  "bboxPx": [
                    450,
                    2178,
                    459,
                    2188
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    163.636,
                    792.0,
                    166.909,
                    795.636
                  ]
                },
                {
                  "bboxPx": [
                    463,
                    2178,
                    489,
                    2212
                  ],
                  "pixelArea": 492,
                  "bboxDp": [
                    168.364,
                    792.0,
                    177.818,
                    804.364
                  ]
                },
                {
                  "bboxPx": [
                    590,
                    2178,
                    631,
                    2221
                  ],
                  "pixelArea": 801,
                  "bboxDp": [
                    214.545,
                    792.0,
                    229.455,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    448,
                    2194,
                    491,
                    2220
                  ],
                  "pixelArea": 272,
                  "bboxDp": [
                    162.909,
                    797.818,
                    178.545,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    543,
                    2197,
                    584,
                    2220
                  ],
                  "pixelArea": 434,
                  "bboxDp": [
                    197.455,
                    798.909,
                    212.364,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    594,
                    2200,
                    604,
                    2207
                  ],
                  "pixelArea": 35,
                  "bboxDp": [
                    216.0,
                    800.0,
                    219.636,
                    802.545
                  ]
                }
              ],
              "totalQualifyingRegions": 55
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    884,
                    1200,
                    1008,
                    1269
                  ],
                  "pixelArea": 6287,
                  "bboxDp": [
                    321.455,
                    436.364,
                    366.545,
                    461.455
                  ]
                },
                {
                  "bboxPx": [
                    78,
                    2132,
                    1002,
                    2260
                  ],
                  "pixelArea": 109288,
                  "bboxDp": [
                    28.364,
                    775.273,
                    364.364,
                    821.818
                  ]
                },
                {
                  "bboxPx": [
                    560,
                    2180,
                    568,
                    2188
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    203.636,
                    792.727,
                    206.545,
                    795.636
                  ]
                },
                {
                  "bboxPx": [
                    598,
                    2199,
                    607,
                    2207
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    217.455,
                    799.636,
                    220.727,
                    802.545
                  ]
                },
                {
                  "bboxPx": [
                    614,
                    2199,
                    623,
                    2204
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    223.273,
                    799.636,
                    226.545,
                    801.455
                  ]
                },
                {
                  "bboxPx": [
                    559,
                    2210,
                    568,
                    2215
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    203.273,
                    803.636,
                    206.545,
                    805.455
                  ]
                }
              ],
              "totalQualifyingRegions": 6
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    959,
                    553,
                    975,
                    580
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    348.727,
                    201.091,
                    354.545,
                    210.909
                  ]
                },
                {
                  "bboxPx": [
                    959,
                    811,
                    975,
                    838
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    348.727,
                    294.909,
                    354.545,
                    304.727
                  ]
                },
                {
                  "bboxPx": [
                    959,
                    1033,
                    975,
                    1060
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    348.727,
                    375.636,
                    354.545,
                    385.455
                  ]
                },
                {
                  "bboxPx": [
                    959,
                    1409,
                    975,
                    1436
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    348.727,
                    512.364,
                    354.545,
                    522.182
                  ]
                }
              ],
              "totalQualifyingRegions": 4
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    773,
                    551,
                    804,
                    586
                  ],
                  "pixelArea": 248,
                  "bboxDp": [
                    281.091,
                    200.364,
                    292.364,
                    213.091
                  ]
                },
                {
                  "bboxPx": [
                    850,
                    551,
                    883,
                    586
                  ],
                  "pixelArea": 199,
                  "bboxDp": [
                    309.091,
                    200.364,
                    321.091,
                    213.091
                  ]
                },
                {
                  "bboxPx": [
                    902,
                    551,
                    923,
                    586
                  ],
                  "pixelArea": 200,
                  "bboxDp": [
                    328.0,
                    200.364,
                    335.636,
                    213.091
                  ]
                },
                {
                  "bboxPx": [
                    812,
                    552,
                    843,
                    586
                  ],
                  "pixelArea": 221,
                  "bboxDp": [
                    295.273,
                    200.727,
                    306.545,
                    213.091
                  ]
                },
                {
                  "bboxPx": [
                    890,
                    552,
                    900,
                    586
                  ],
                  "pixelArea": 69,
                  "bboxDp": [
                    323.636,
                    200.727,
                    327.273,
                    213.091
                  ]
                },
                {
                  "bboxPx": [
                    817,
                    559,
                    838,
                    578
                  ],
                  "pixelArea": 125,
                  "bboxDp": [
                    297.091,
                    203.273,
                    304.727,
                    210.182
                  ]
                },
                {
                  "bboxPx": [
                    112,
                    1066,
                    130,
                    1073
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    40.727,
                    387.636,
                    47.273,
                    390.182
                  ]
                },
                {
                  "bboxPx": [
                    218,
                    1066,
                    247,
                    1097
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    79.273,
                    387.636,
                    89.818,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    374,
                    1066,
                    403,
                    1097
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    136.0,
                    387.636,
                    146.545,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    488,
                    1066,
                    502,
                    1093
                  ],
                  "pixelArea": 103,
                  "bboxDp": [
                    177.455,
                    387.636,
                    182.545,
                    397.455
                  ]
                },
                {
                  "bboxPx": [
                    506,
                    1066,
                    535,
                    1097
                  ],
                  "pixelArea": 215,
                  "bboxDp": [
                    184.0,
                    387.636,
                    194.545,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    626,
                    1066,
                    646,
                    1088
                  ],
                  "pixelArea": 106,
                  "bboxDp": [
                    227.636,
                    387.636,
                    234.909,
                    395.636
                  ]
                },
                {
                  "bboxPx": [
                    88,
                    1067,
                    103,
                    1078
                  ],
                  "pixelArea": 37,
                  "bboxDp": [
                    32.0,
                    388.0,
                    37.455,
                    392.0
                  ]
                },
                {
                  "bboxPx": [
                    174,
                    1067,
                    201,
                    1078
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    63.273,
                    388.0,
                    73.091,
                    392.0
                  ]
                },
                {
                  "bboxPx": [
                    252,
                    1067,
                    279,
                    1075
                  ],
                  "pixelArea": 74,
                  "bboxDp": [
                    91.636,
                    388.0,
                    101.455,
                    390.909
                  ]
                },
                {
                  "bboxPx": [
                    296,
                    1067,
                    310,
                    1097
                  ],
                  "pixelArea": 80,
                  "bboxDp": [
                    107.636,
                    388.0,
                    112.727,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    455,
                    1067,
                    469,
                    1096
                  ],
                  "pixelArea": 90,
                  "bboxDp": [
                    165.455,
                    388.0,
                    170.545,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    597,
                    1067,
                    613,
                    1096
                  ],
                  "pixelArea": 67,
                  "bboxDp": [
                    217.091,
                    388.0,
                    222.909,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    552,
                    1068,
                    579,
                    1097
                  ],
                  "pixelArea": 168,
                  "bboxDp": [
                    200.727,
                    388.364,
                    210.545,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    651,
                    1068,
                    677,
                    1096
                  ],
                  "pixelArea": 193,
                  "bboxDp": [
                    236.727,
                    388.364,
                    246.182,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    140,
                    1069,
                    169,
                    1097
                  ],
                  "pixelArea": 92,
                  "bboxDp": [
                    50.909,
                    388.727,
                    61.455,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    585,
                    1069,
                    595,
                    1093
                  ],
                  "pixelArea": 115,
                  "bboxDp": [
                    212.727,
                    388.727,
                    216.364,
                    397.455
                  ]
                },
                {
                  "bboxPx": [
                    312,
                    1070,
                    323,
                    1095
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    113.455,
                    389.091,
                    117.455,
                    398.182
                  ]
                },
                {
                  "bboxPx": [
                    107,
                    1074,
                    126,
                    1082
                  ],
                  "pixelArea": 36,
                  "bboxDp": [
                    38.909,
                    390.545,
                    45.818,
                    393.455
                  ]
                },
                {
                  "bboxPx": [
                    256,
                    1075,
                    275,
                    1080
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    93.091,
                    390.909,
                    100.0,
                    392.727
                  ]
                },
                {
                  "bboxPx": [
                    144,
                    1076,
                    156,
                    1091
                  ],
                  "pixelArea": 59,
                  "bboxDp": [
                    52.364,
                    391.273,
                    56.727,
                    396.727
                  ]
                },
                {
                  "bboxPx": [
                    183,
                    1076,
                    201,
                    1096
                  ],
                  "pixelArea": 100,
                  "bboxDp": [
                    66.545,
                    391.273,
                    73.091,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    416,
                    1076,
                    436,
                    1090
                  ],
                  "pixelArea": 89,
                  "bboxDp": [
                    151.273,
                    391.273,
                    158.545,
                    396.364
                  ]
                },
                {
                  "bboxPx": [
                    74,
                    1077,
                    84,
                    1096
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    26.909,
                    391.636,
                    30.545,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    440,
                    1077,
                    454,
                    1092
                  ],
                  "pixelArea": 66,
                  "bboxDp": [
                    160.0,
                    391.636,
                    165.091,
                    397.091
                  ]
                },
                {
                  "bboxPx": [
                    559,
                    1079,
                    572,
                    1092
                  ],
                  "pixelArea": 64,
                  "bboxDp": [
                    203.273,
                    392.364,
                    208.0,
                    397.091
                  ]
                },
                {
                  "bboxPx": [
                    264,
                    1080,
                    274,
                    1095
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    96.0,
                    392.727,
                    99.636,
                    398.182
                  ]
                },
                {
                  "bboxPx": [
                    111,
                    1083,
                    132,
                    1097
                  ],
                  "pixelArea": 152,
                  "bboxDp": [
                    40.364,
                    393.818,
                    48.0,
                    398.909
                  ]
                },
                {
                  "bboxPx": [
                    87,
                    1090,
                    103,
                    1096
                  ],
                  "pixelArea": 28,
                  "bboxDp": [
                    31.636,
                    396.364,
                    37.455,
                    398.545
                  ]
                },
                {
                  "bboxPx": [
                    206,
                    1254,
                    217,
                    1285
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    74.909,
                    456.0,
                    78.909,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    355,
                    1254,
                    367,
                    1268
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    129.091,
                    456.0,
                    133.455,
                    461.091
                  ]
                },
                {
                  "bboxPx": [
                    416,
                    1254,
                    433,
                    1267
                  ],
                  "pixelArea": 72,
                  "bboxDp": [
                    151.273,
                    456.0,
                    157.455,
                    460.727
                  ]
                },
                {
                  "bboxPx": [
                    446,
                    1254,
                    465,
                    1279
                  ],
                  "pixelArea": 91,
                  "bboxDp": [
                    162.182,
                    456.0,
                    169.091,
                    465.091
                  ]
                },
                {
                  "bboxPx": [
                    611,
                    1254,
                    631,
                    1285
                  ],
                  "pixelArea": 137,
                  "bboxDp": [
                    222.182,
                    456.0,
                    229.455,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    75,
                    1255,
                    102,
                    1284
                  ],
                  "pixelArea": 163,
                  "bboxDp": [
                    27.273,
                    456.364,
                    37.091,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    173,
                    1255,
                    188,
                    1280
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    62.909,
                    456.364,
                    68.364,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    241,
                    1255,
                    249,
                    1285
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    87.636,
                    456.364,
                    90.545,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    251,
                    1255,
                    266,
                    1285
                  ],
                  "pixelArea": 110,
                  "bboxDp": [
                    91.273,
                    456.364,
                    96.727,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    372,
                    1255,
                    387,
                    1267
                  ],
                  "pixelArea": 29,
                  "bboxDp": [
                    135.273,
                    456.364,
                    140.727,
                    460.727
                  ]
                },
                {
                  "bboxPx": [
                    405,
                    1255,
                    415,
                    1262
                  ],
                  "pixelArea": 25,
                  "bboxDp": [
                    147.273,
                    456.364,
                    150.909,
                    458.909
                  ]
                },
                {
                  "bboxPx": [
                    470,
                    1255,
                    499,
                    1284
                  ],
                  "pixelArea": 274,
                  "bboxDp": [
                    170.909,
                    456.364,
                    181.455,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    569,
                    1255,
                    584,
                    1280
                  ],
                  "pixelArea": 81,
                  "bboxDp": [
                    206.909,
                    456.364,
                    212.364,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    117,
                    1256,
                    135,
                    1285
                  ],
                  "pixelArea": 91,
                  "bboxDp": [
                    42.545,
                    456.727,
                    49.091,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    503,
                    1256,
                    532,
                    1277
                  ],
                  "pixelArea": 186,
                  "bboxDp": [
                    182.909,
                    456.727,
                    193.455,
                    464.364
                  ]
                },
                {
                  "bboxPx": [
                    190,
                    1257,
                    200,
                    1284
                  ],
                  "pixelArea": 79,
                  "bboxDp": [
                    69.091,
                    457.091,
                    72.727,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    218,
                    1257,
                    234,
                    1270
                  ],
                  "pixelArea": 58,
                  "bboxDp": [
                    79.273,
                    457.091,
                    85.091,
                    461.818
                  ]
                },
                {
                  "bboxPx": [
                    305,
                    1257,
                    334,
                    1285
                  ],
                  "pixelArea": 92,
                  "bboxDp": [
                    110.909,
                    457.091,
                    121.455,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    339,
                    1257,
                    352,
                    1263
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    123.273,
                    457.091,
                    128.0,
                    459.273
                  ]
                },
                {
                  "bboxPx": [
                    586,
                    1257,
                    596,
                    1284
                  ],
                  "pixelArea": 79,
                  "bboxDp": [
                    213.091,
                    457.091,
                    216.727,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    150,
                    1260,
                    168,
                    1280
                  ],
                  "pixelArea": 147,
                  "bboxDp": [
                    54.545,
                    458.182,
                    61.091,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    546,
                    1260,
                    564,
                    1280
                  ],
                  "pixelArea": 147,
                  "bboxDp": [
                    198.545,
                    458.182,
                    205.091,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    113,
                    1261,
                    130,
                    1282
                  ],
                  "pixelArea": 71,
                  "bboxDp": [
                    41.091,
                    458.545,
                    47.273,
                    466.182
                  ]
                },
                {
                  "bboxPx": [
                    309,
                    1264,
                    321,
                    1279
                  ],
                  "pixelArea": 59,
                  "bboxDp": [
                    112.364,
                    459.636,
                    116.727,
                    465.091
                  ]
                },
                {
                  "bboxPx": [
                    405,
                    1264,
                    415,
                    1284
                  ],
                  "pixelArea": 61,
                  "bboxDp": [
                    147.273,
                    459.636,
                    150.909,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    340,
                    1266,
                    351,
                    1285
                  ],
                  "pixelArea": 95,
                  "bboxDp": [
                    123.636,
                    460.364,
                    127.636,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    437,
                    1267,
                    446,
                    1281
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    158.909,
                    460.727,
                    162.182,
                    465.818
                  ]
                },
                {
                  "bboxPx": [
                    223,
                    1270,
                    235,
                    1284
                  ],
                  "pixelArea": 31,
                  "bboxDp": [
                    81.091,
                    461.818,
                    85.455,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    355,
                    1270,
                    366,
                    1284
                  ],
                  "pixelArea": 30,
                  "bboxDp": [
                    129.091,
                    461.818,
                    133.091,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    372,
                    1271,
                    399,
                    1276
                  ],
                  "pixelArea": 61,
                  "bboxDp": [
                    135.273,
                    462.182,
                    145.091,
                    464.0
                  ]
                },
                {
                  "bboxPx": [
                    418,
                    1271,
                    431,
                    1285
                  ],
                  "pixelArea": 75,
                  "bboxDp": [
                    152.0,
                    462.182,
                    156.727,
                    467.273
                  ]
                },
                {
                  "bboxPx": [
                    374,
                    1276,
                    398,
                    1284
                  ],
                  "pixelArea": 52,
                  "bboxDp": [
                    136.0,
                    464.0,
                    144.727,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    508,
                    1276,
                    530,
                    1284
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    184.727,
                    464.0,
                    192.727,
                    466.909
                  ]
                },
                {
                  "bboxPx": [
                    83,
                    1442,
                    103,
                    1464
                  ],
                  "pixelArea": 106,
                  "bboxDp": [
                    30.182,
                    524.364,
                    37.455,
                    532.364
                  ]
                },
                {
                  "bboxPx": [
                    305,
                    1442,
                    317,
                    1473
                  ],
                  "pixelArea": 80,
                  "bboxDp": [
                    110.909,
                    524.364,
                    115.273,
                    535.636
                  ]
                },
                {
                  "bboxPx": [
                    421,
                    1442,
                    433,
                    1456
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    153.091,
                    524.364,
                    157.455,
                    529.455
                  ]
                },
                {
                  "bboxPx": [
                    181,
                    1443,
                    196,
                    1452
                  ],
                  "pixelArea": 43,
                  "bboxDp": [
                    65.818,
                    524.727,
                    71.273,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    286,
                    1443,
                    299,
                    1454
                  ],
                  "pixelArea": 55,
                  "bboxDp": [
                    104.0,
                    524.727,
                    108.727,
                    528.727
                  ]
                },
                {
                  "bboxPx": [
                    339,
                    1443,
                    366,
                    1472
                  ],
                  "pixelArea": 163,
                  "bboxDp": [
                    123.273,
                    524.727,
                    133.091,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    384,
                    1443,
                    400,
                    1472
                  ],
                  "pixelArea": 101,
                  "bboxDp": [
                    139.636,
                    524.727,
                    145.455,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    108,
                    1444,
                    134,
                    1472
                  ],
                  "pixelArea": 193,
                  "bboxDp": [
                    39.273,
                    525.091,
                    48.727,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    141,
                    1444,
                    168,
                    1470
                  ],
                  "pixelArea": 204,
                  "bboxDp": [
                    51.273,
                    525.091,
                    61.091,
                    534.545
                  ]
                },
                {
                  "bboxPx": [
                    240,
                    1444,
                    248,
                    1473
                  ],
                  "pixelArea": 42,
                  "bboxDp": [
                    87.273,
                    525.091,
                    90.182,
                    535.636
                  ]
                },
                {
                  "bboxPx": [
                    272,
                    1444,
                    283,
                    1471
                  ],
                  "pixelArea": 96,
                  "bboxDp": [
                    98.909,
                    525.091,
                    102.909,
                    534.909
                  ]
                },
                {
                  "bboxPx": [
                    319,
                    1444,
                    333,
                    1472
                  ],
                  "pixelArea": 138,
                  "bboxDp": [
                    116.0,
                    525.091,
                    121.091,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    206,
                    1445,
                    235,
                    1472
                  ],
                  "pixelArea": 157,
                  "bboxDp": [
                    74.909,
                    525.455,
                    85.455,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    405,
                    1445,
                    418,
                    1451
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    147.273,
                    525.455,
                    152.0,
                    527.636
                  ]
                },
                {
                  "bboxPx": [
                    371,
                    1446,
                    383,
                    1466
                  ],
                  "pixelArea": 70,
                  "bboxDp": [
                    134.909,
                    525.818,
                    139.273,
                    533.091
                  ]
                },
                {
                  "bboxPx": [
                    249,
                    1447,
                    268,
                    1452
                  ],
                  "pixelArea": 44,
                  "bboxDp": [
                    90.545,
                    526.182,
                    97.455,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    175,
                    1452,
                    190,
                    1459
                  ],
                  "pixelArea": 25,
                  "bboxDp": [
                    63.636,
                    528.0,
                    69.091,
                    530.545
                  ]
                },
                {
                  "bboxPx": [
                    249,
                    1454,
                    268,
                    1473
                  ],
                  "pixelArea": 84,
                  "bboxDp": [
                    90.545,
                    528.727,
                    97.455,
                    535.636
                  ]
                },
                {
                  "bboxPx": [
                    406,
                    1454,
                    417,
                    1473
                  ],
                  "pixelArea": 95,
                  "bboxDp": [
                    147.636,
                    528.727,
                    151.636,
                    535.636
                  ]
                },
                {
                  "bboxPx": [
                    188,
                    1455,
                    201,
                    1462
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    68.364,
                    529.091,
                    73.091,
                    531.636
                  ]
                },
                {
                  "bboxPx": [
                    284,
                    1457,
                    301,
                    1473
                  ],
                  "pixelArea": 57,
                  "bboxDp": [
                    103.273,
                    529.818,
                    109.455,
                    535.636
                  ]
                },
                {
                  "bboxPx": [
                    421,
                    1458,
                    432,
                    1472
                  ],
                  "pixelArea": 30,
                  "bboxDp": [
                    153.091,
                    530.182,
                    157.091,
                    535.273
                  ]
                },
                {
                  "bboxPx": [
                    175,
                    1464,
                    196,
                    1473
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    63.636,
                    532.364,
                    71.273,
                    535.636
                  ]
                }
              ],
              "totalQualifyingRegions": 90
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                78,
                190,
                141,
                235
              ],
              "bboxDp": [
                28.364,
                69.091,
                51.273,
                85.455
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                82,
                332,
                99,
                349
              ],
              "bboxDp": [
                29.818,
                120.727,
                36.0,
                126.909
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                75,
                332,
                155,
                412
              ],
              "bboxDp": [
                27.273,
                120.727,
                56.364,
                149.818
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                164,
                334,
                242,
                409
              ],
              "bboxDp": [
                59.636,
                121.455,
                88.0,
                148.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                548,
                158,
                589
              ],
              "bboxDp": [
                26.545,
                199.273,
                57.455,
                214.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                806,
                378,
                848
              ],
              "bboxDp": [
                26.545,
                293.091,
                137.455,
                308.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                1004,
                510,
                1046
              ],
              "bboxDp": [
                26.545,
                365.091,
                185.455,
                380.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1192,
                288,
                1234
              ],
              "bboxDp": [
                26.909,
                433.455,
                104.727,
                448.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1380,
                290,
                1422
              ],
              "bboxDp": [
                26.909,
                501.818,
                105.455,
                517.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        },
        "reference": {
          "flatColorRegions": {
            "white-surface": {
              "exactRgb": "#FFFFFF",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    0,
                    96,
                    1080,
                    2334
                  ],
                  "pixelArea": 2221590,
                  "bboxDp": [
                    0.0,
                    34.909,
                    392.727,
                    848.727
                  ]
                },
                {
                  "bboxPx": [
                    177,
                    339,
                    190,
                    346
                  ],
                  "pixelArea": 91,
                  "bboxDp": [
                    64.364,
                    123.273,
                    69.091,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    197,
                    339,
                    211,
                    346
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    71.636,
                    123.273,
                    76.727,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    218,
                    339,
                    232,
                    346
                  ],
                  "pixelArea": 98,
                  "bboxDp": [
                    79.273,
                    123.273,
                    84.364,
                    125.818
                  ]
                },
                {
                  "bboxPx": [
                    117,
                    376,
                    142,
                    392
                  ],
                  "pixelArea": 238,
                  "bboxDp": [
                    42.545,
                    136.727,
                    51.636,
                    142.545
                  ]
                },
                {
                  "bboxPx": [
                    183,
                    399,
                    224,
                    404
                  ],
                  "pixelArea": 205,
                  "bboxDp": [
                    66.545,
                    145.091,
                    81.455,
                    146.909
                  ]
                },
                {
                  "bboxPx": [
                    819,
                    555,
                    844,
                    582
                  ],
                  "pixelArea": 374,
                  "bboxDp": [
                    297.818,
                    201.818,
                    306.909,
                    211.636
                  ]
                },
                {
                  "bboxPx": [
                    125,
                    556,
                    147,
                    577
                  ],
                  "pixelArea": 198,
                  "bboxDp": [
                    45.455,
                    202.182,
                    53.455,
                    209.818
                  ]
                },
                {
                  "bboxPx": [
                    781,
                    561,
                    791,
                    570
                  ],
                  "pixelArea": 90,
                  "bboxDp": [
                    284.0,
                    204.0,
                    287.636,
                    207.273
                  ]
                },
                {
                  "bboxPx": [
                    795,
                    561,
                    805,
                    570
                  ],
                  "pixelArea": 90,
                  "bboxDp": [
                    289.091,
                    204.0,
                    292.727,
                    207.273
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    567,
                    110,
                    584
                  ],
                  "pixelArea": 100,
                  "bboxDp": [
                    34.909,
                    206.182,
                    40.0,
                    212.364
                  ]
                },
                {
                  "bboxPx": [
                    214,
                    815,
                    226,
                    821
                  ],
                  "pixelArea": 22,
                  "bboxDp": [
                    77.818,
                    296.364,
                    82.182,
                    298.545
                  ]
                },
                {
                  "bboxPx": [
                    126,
                    821,
                    142,
                    837
                  ],
                  "pixelArea": 102,
                  "bboxDp": [
                    45.818,
                    298.545,
                    51.636,
                    304.364
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    824,
                    104,
                    832
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    34.909,
                    299.636,
                    37.818,
                    302.545
                  ]
                },
                {
                  "bboxPx": [
                    180,
                    835,
                    188,
                    842
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    65.455,
                    303.636,
                    68.364,
                    306.182
                  ]
                },
                {
                  "bboxPx": [
                    353,
                    1011,
                    363,
                    1037
                  ],
                  "pixelArea": 112,
                  "bboxDp": [
                    128.364,
                    367.636,
                    132.0,
                    377.091
                  ]
                },
                {
                  "bboxPx": [
                    214,
                    1013,
                    226,
                    1019
                  ],
                  "pixelArea": 22,
                  "bboxDp": [
                    77.818,
                    368.364,
                    82.182,
                    370.545
                  ]
                },
                {
                  "bboxPx": [
                    494,
                    1013,
                    503,
                    1027
                  ],
                  "pixelArea": 83,
                  "bboxDp": [
                    179.636,
                    368.364,
                    182.909,
                    373.455
                  ]
                },
                {
                  "bboxPx": [
                    126,
                    1019,
                    142,
                    1035
                  ],
                  "pixelArea": 102,
                  "bboxDp": [
                    45.818,
                    370.545,
                    51.636,
                    376.364
                  ]
                },
                {
                  "bboxPx": [
                    96,
                    1022,
                    104,
                    1030
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    34.909,
                    371.636,
                    37.818,
                    374.545
                  ]
                },
                {
                  "bboxPx": [
                    272,
                    1029,
                    280,
                    1034
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    98.909,
                    374.182,
                    101.818,
                    376.0
                  ]
                },
                {
                  "bboxPx": [
                    180,
                    1033,
                    188,
                    1040
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    65.455,
                    375.636,
                    68.364,
                    378.182
                  ]
                },
                {
                  "bboxPx": [
                    666,
                    1071,
                    674,
                    1076
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    242.182,
                    389.455,
                    245.091,
                    391.273
                  ]
                },
                {
                  "bboxPx": [
                    511,
                    1072,
                    521,
                    1078
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    185.818,
                    389.818,
                    189.455,
                    392.0
                  ]
                },
                {
                  "bboxPx": [
                    624,
                    1072,
                    634,
                    1094
                  ],
                  "pixelArea": 84,
                  "bboxDp": [
                    226.909,
                    389.818,
                    230.545,
                    397.818
                  ]
                },
                {
                  "bboxPx": [
                    445,
                    1077,
                    459,
                    1091
                  ],
                  "pixelArea": 87,
                  "bboxDp": [
                    161.818,
                    391.636,
                    166.909,
                    396.727
                  ]
                },
                {
                  "bboxPx": [
                    146,
                    1079,
                    154,
                    1086
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    53.091,
                    392.364,
                    56.0,
                    394.909
                  ]
                },
                {
                  "bboxPx": [
                    421,
                    1079,
                    430,
                    1087
                  ],
                  "pixelArea": 37,
                  "bboxDp": [
                    153.091,
                    392.364,
                    156.364,
                    395.273
                  ]
                },
                {
                  "bboxPx": [
                    666,
                    1079,
                    674,
                    1084
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    242.182,
                    392.364,
                    245.091,
                    394.182
                  ]
                },
                {
                  "bboxPx": [
                    510,
                    1081,
                    521,
                    1092
                  ],
                  "pixelArea": 35,
                  "bboxDp": [
                    185.455,
                    393.091,
                    189.455,
                    397.091
                  ]
                },
                {
                  "bboxPx": [
                    263,
                    1215,
                    271,
                    1225
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    95.636,
                    441.818,
                    98.545,
                    445.455
                  ]
                },
                {
                  "bboxPx": [
                    955,
                    1216,
                    992,
                    1253
                  ],
                  "pixelArea": 1021,
                  "bboxDp": [
                    347.273,
                    442.182,
                    360.727,
                    455.636
                  ]
                },
                {
                  "bboxPx": [
                    222,
                    1220,
                    243,
                    1228
                  ],
                  "pixelArea": 53,
                  "bboxDp": [
                    80.727,
                    443.636,
                    88.364,
                    446.545
                  ]
                },
                {
                  "bboxPx": [
                    380,
                    1259,
                    391,
                    1264
                  ],
                  "pixelArea": 28,
                  "bboxDp": [
                    138.182,
                    457.818,
                    142.182,
                    459.636
                  ]
                },
                {
                  "bboxPx": [
                    222,
                    1260,
                    231,
                    1273
                  ],
                  "pixelArea": 73,
                  "bboxDp": [
                    80.727,
                    458.182,
                    84.0,
                    462.909
                  ]
                },
                {
                  "bboxPx": [
                    311,
                    1267,
                    319,
                    1274
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    113.091,
                    460.727,
                    116.0,
                    463.273
                  ]
                },
                {
                  "bboxPx": [
                    381,
                    1274,
                    392,
                    1280
                  ],
                  "pixelArea": 46,
                  "bboxDp": [
                    138.545,
                    463.273,
                    142.545,
                    465.455
                  ]
                },
                {
                  "bboxPx": [
                    153,
                    1275,
                    166,
                    1282
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    55.636,
                    463.636,
                    60.364,
                    466.182
                  ]
                },
                {
                  "bboxPx": [
                    549,
                    1275,
                    562,
                    1282
                  ],
                  "pixelArea": 56,
                  "bboxDp": [
                    199.636,
                    463.636,
                    204.364,
                    466.182
                  ]
                },
                {
                  "bboxPx": [
                    228,
                    1405,
                    236,
                    1410
                  ],
                  "pixelArea": 24,
                  "bboxDp": [
                    82.909,
                    510.909,
                    85.818,
                    512.727
                  ]
                },
                {
                  "bboxPx": [
                    123,
                    1447,
                    131,
                    1452
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    44.727,
                    526.182,
                    47.636,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1447,
                    331,
                    1452
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    526.182,
                    120.364,
                    528.0
                  ]
                },
                {
                  "bboxPx": [
                    81,
                    1448,
                    91,
                    1470
                  ],
                  "pixelArea": 84,
                  "bboxDp": [
                    29.455,
                    526.545,
                    33.091,
                    534.545
                  ]
                },
                {
                  "bboxPx": [
                    123,
                    1455,
                    131,
                    1460
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    44.727,
                    529.091,
                    47.636,
                    530.909
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1455,
                    331,
                    1460
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    529.091,
                    120.364,
                    530.909
                  ]
                },
                {
                  "bboxPx": [
                    188,
                    1460,
                    197,
                    1465
                  ],
                  "pixelArea": 27,
                  "bboxDp": [
                    68.364,
                    530.909,
                    71.636,
                    532.727
                  ]
                },
                {
                  "bboxPx": [
                    321,
                    1463,
                    331,
                    1468
                  ],
                  "pixelArea": 50,
                  "bboxDp": [
                    116.727,
                    532.0,
                    120.364,
                    533.818
                  ]
                },
                {
                  "bboxPx": [
                    498,
                    2177,
                    536,
                    2221
                  ],
                  "pixelArea": 771,
                  "bboxDp": [
                    181.091,
                    791.636,
                    194.909,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    542,
                    2177,
                    585,
                    2199
                  ],
                  "pixelArea": 417,
                  "bboxDp": [
                    197.091,
                    791.636,
                    212.727,
                    799.636
                  ]
                },
                {
                  "bboxPx": [
                    452,
                    2178,
                    461,
                    2188
                  ],
                  "pixelArea": 40,
                  "bboxDp": [
                    164.364,
                    792.0,
                    167.636,
                    795.636
                  ]
                },
                {
                  "bboxPx": [
                    465,
                    2178,
                    491,
                    2212
                  ],
                  "pixelArea": 493,
                  "bboxDp": [
                    169.091,
                    792.0,
                    178.545,
                    804.364
                  ]
                },
                {
                  "bboxPx": [
                    589,
                    2178,
                    630,
                    2221
                  ],
                  "pixelArea": 795,
                  "bboxDp": [
                    214.182,
                    792.0,
                    229.091,
                    807.636
                  ]
                },
                {
                  "bboxPx": [
                    450,
                    2194,
                    492,
                    2220
                  ],
                  "pixelArea": 269,
                  "bboxDp": [
                    163.636,
                    797.818,
                    178.909,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    543,
                    2197,
                    584,
                    2220
                  ],
                  "pixelArea": 430,
                  "bboxDp": [
                    197.455,
                    798.909,
                    212.364,
                    807.273
                  ]
                },
                {
                  "bboxPx": [
                    593,
                    2200,
                    602,
                    2208
                  ],
                  "pixelArea": 34,
                  "bboxDp": [
                    215.636,
                    800.0,
                    218.909,
                    802.909
                  ]
                }
              ],
              "totalQualifyingRegions": 55
            },
            "rn-page": {
              "exactRgb": "#F6F7F9",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "native-page": {
              "exactRgb": "#FAFAFA",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "brand-day": {
              "exactRgb": "#FF612E",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [
                {
                  "bboxPx": [
                    884,
                    1200,
                    1008,
                    1269
                  ],
                  "pixelArea": 6249,
                  "bboxDp": [
                    321.455,
                    436.364,
                    366.545,
                    461.455
                  ]
                },
                {
                  "bboxPx": [
                    78,
                    2132,
                    1002,
                    2260
                  ],
                  "pixelArea": 109279,
                  "bboxDp": [
                    28.364,
                    775.273,
                    364.364,
                    821.818
                  ]
                },
                {
                  "bboxPx": [
                    560,
                    2180,
                    568,
                    2188
                  ],
                  "pixelArea": 33,
                  "bboxDp": [
                    203.636,
                    792.727,
                    206.545,
                    795.636
                  ]
                },
                {
                  "bboxPx": [
                    597,
                    2199,
                    606,
                    2206
                  ],
                  "pixelArea": 32,
                  "bboxDp": [
                    217.091,
                    799.636,
                    220.364,
                    802.182
                  ]
                },
                {
                  "bboxPx": [
                    613,
                    2199,
                    621,
                    2204
                  ],
                  "pixelArea": 26,
                  "bboxDp": [
                    222.909,
                    799.636,
                    225.818,
                    801.455
                  ]
                },
                {
                  "bboxPx": [
                    559,
                    2210,
                    568,
                    2215
                  ],
                  "pixelArea": 38,
                  "bboxDp": [
                    203.273,
                    803.636,
                    206.545,
                    805.455
                  ]
                }
              ],
              "totalQualifyingRegions": 6
            },
            "switch-track-candidate": {
              "exactRgb": "#E6E6E6",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            },
            "secondary-ink-candidate": {
              "exactRgb": "#999999",
              "tolerancePerChannel": 0,
              "interpretation": "color component only; semantic role needs source/hierarchy",
              "regions": [],
              "totalQualifyingRegions": 0
            }
          },
          "inkRegions": [
            {
              "bboxPx": [
                75,
                188,
                147,
                237
              ],
              "bboxDp": [
                27.273,
                68.364,
                53.455,
                86.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                83,
                332,
                100,
                349
              ],
              "bboxDp": [
                30.182,
                120.727,
                36.364,
                126.909
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                76,
                332,
                156,
                412
              ],
              "bboxDp": [
                27.636,
                120.727,
                56.727,
                149.818
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                165,
                334,
                243,
                409
              ],
              "bboxDp": [
                60.0,
                121.455,
                88.364,
                148.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                548,
                158,
                589
              ],
              "bboxDp": [
                26.545,
                199.273,
                57.455,
                214.182
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                806,
                378,
                848
              ],
              "bboxDp": [
                26.545,
                293.091,
                137.455,
                308.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                73,
                1004,
                510,
                1046
              ],
              "bboxDp": [
                26.545,
                365.091,
                185.455,
                380.364
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1192,
                288,
                1234
              ],
              "bboxDp": [
                26.909,
                433.455,
                104.727,
                448.727
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            },
            {
              "bboxPx": [
                74,
                1380,
                290,
                1422
              ],
              "bboxDp": [
                26.909,
                501.818,
                105.455,
                517.091
              ],
              "classification": "text-or-icon-ink candidate; no OCR/semantic identification"
            }
          ],
          "inkMethod": "RGB channel maximum <=110, 9px square dilation, refit to undilated ink; text and icons are not distinguished"
        }
      },
      "inkPositionCandidates": [
        {
          "originalRegion": 0,
          "referenceRegion": 0,
          "originalBboxPx": [
            78,
            190,
            141,
            235
          ],
          "referenceBboxPx": [
            75,
            188,
            147,
            237
          ],
          "edgeDeltaPx": [
            -3,
            -2,
            6,
            2
          ],
          "edgeDeltaDp": [
            -1.091,
            -0.727,
            2.182,
            0.727
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 2,
          "referenceRegion": 2,
          "originalBboxPx": [
            75,
            332,
            155,
            412
          ],
          "referenceBboxPx": [
            76,
            332,
            156,
            412
          ],
          "edgeDeltaPx": [
            1,
            0,
            1,
            0
          ],
          "edgeDeltaDp": [
            0.364,
            0.0,
            0.364,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 1,
          "referenceRegion": 1,
          "originalBboxPx": [
            82,
            332,
            99,
            349
          ],
          "referenceBboxPx": [
            83,
            332,
            100,
            349
          ],
          "edgeDeltaPx": [
            1,
            0,
            1,
            0
          ],
          "edgeDeltaDp": [
            0.364,
            0.0,
            0.364,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 3,
          "referenceRegion": 3,
          "originalBboxPx": [
            164,
            334,
            242,
            409
          ],
          "referenceBboxPx": [
            165,
            334,
            243,
            409
          ],
          "edgeDeltaPx": [
            1,
            0,
            1,
            0
          ],
          "edgeDeltaDp": [
            0.364,
            0.0,
            0.364,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 4,
          "referenceRegion": 4,
          "originalBboxPx": [
            73,
            548,
            158,
            589
          ],
          "referenceBboxPx": [
            73,
            548,
            158,
            589
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 5,
          "referenceRegion": 5,
          "originalBboxPx": [
            73,
            806,
            378,
            848
          ],
          "referenceBboxPx": [
            73,
            806,
            378,
            848
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 6,
          "referenceRegion": 6,
          "originalBboxPx": [
            73,
            1004,
            510,
            1046
          ],
          "referenceBboxPx": [
            73,
            1004,
            510,
            1046
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 7,
          "referenceRegion": 7,
          "originalBboxPx": [
            74,
            1192,
            288,
            1234
          ],
          "referenceBboxPx": [
            74,
            1192,
            288,
            1234
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        },
        {
          "originalRegion": 8,
          "referenceRegion": 8,
          "originalBboxPx": [
            74,
            1380,
            290,
            1422
          ],
          "referenceBboxPx": [
            74,
            1380,
            290,
            1422
          ],
          "edgeDeltaPx": [
            0,
            0,
            0,
            0
          ],
          "edgeDeltaDp": [
            0.0,
            0.0,
            0.0,
            0.0
          ],
          "status": "unverified geometric candidate pair; confirm same label/control with hierarchy"
        }
      ],
      "manualReviewInputMatches": true,
      "components": [
        {
          "id": "ChevronRow",
          "page": "settings-day",
          "cropBoxPx": [
            64,
            485,
            1016,
            648
          ],
          "sourceBoundsPx": [
            72,
            493,
            1008,
            640
          ],
          "basis": "spec.md original region row and 8px context",
          "notes": "Native single-line trailing-value variant; other variants are visible in full pages.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "209f4bac18cf8c5da8d2eb25263dcad04def3b487a3de02b904bc3ef46d254e3",
            "reference": "f6bc65c3fb97598581853ed2483cdf379d663b3a37d575d246026060167eec11"
          },
          "originalEvidenceId": "ev_627d382d9998bb8838a229e02cf660f5c7aef7aef8c1871f4720c2e113943320",
          "referenceEvidenceId": "ev_086cd80eceb67a3de28f9d1e949e6fdcac174bc534980b326a7eba2c007a7feb",
          "images": {
            "sideBySide": {
              "path": "generated/component-ChevronRow-side-by-side.png",
              "sha256": "31f910cbaf8499de073841372ad4b539ce8f9a39f787a609fa5e667d446a787b",
              "canvasPx": [
                1904,
                163
              ]
            },
            "overlay": {
              "path": "generated/component-ChevronRow-overlay.png",
              "sha256": "a37ab99232d8810bc986c848c0dc8c587dbe5936506da5b696c8c230d34759eb",
              "canvasPx": [
                952,
                163
              ]
            },
            "difference": {
              "path": "generated/component-ChevronRow-difference-x3.png",
              "sha256": "b1324a54dbee215dfa73ab1cbf4e483c57bf6ebb122a3865c5cf0e9f706551bd",
              "canvasPx": [
                952,
                163
              ]
            }
          }
        },
        {
          "id": "SwitchRow",
          "page": "settings-day",
          "cropBoxPx": [
            64,
            1133,
            1016,
            1337
          ],
          "sourceBoundsPx": [
            72,
            1141,
            1008,
            1329
          ],
          "basis": "spec.md notification row; orange switch exact extent [884,1200,1008,1269]",
          "notes": "ON is established by orange pixels and description, even when source custom widget reports checked=false. This crop does not verify persistence or permission behavior.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "209f4bac18cf8c5da8d2eb25263dcad04def3b487a3de02b904bc3ef46d254e3",
            "reference": "f6bc65c3fb97598581853ed2483cdf379d663b3a37d575d246026060167eec11"
          },
          "originalEvidenceId": "ev_627d382d9998bb8838a229e02cf660f5c7aef7aef8c1871f4720c2e113943320",
          "referenceEvidenceId": "ev_086cd80eceb67a3de28f9d1e949e6fdcac174bc534980b326a7eba2c007a7feb",
          "images": {
            "sideBySide": {
              "path": "generated/component-SwitchRow-side-by-side.png",
              "sha256": "1fa49e0cd177e61a9a24a320efe1e0978c2f4be9e88461844f832310289c83c4",
              "canvasPx": [
                1904,
                204
              ]
            },
            "overlay": {
              "path": "generated/component-SwitchRow-overlay.png",
              "sha256": "4e3c23c9f6c9ce15d94fa4b167827e50ae5de9ee5fa3584e8ab32db12f950944",
              "canvasPx": [
                952,
                204
              ]
            },
            "difference": {
              "path": "generated/component-SwitchRow-difference-x3.png",
              "sha256": "664634fd873bebb281de09ae4a8b92da77006beaa37d4dd780ee3c9d3d8df151",
              "canvasPx": [
                952,
                204
              ]
            }
          }
        },
        {
          "id": "PrimaryButton",
          "page": "settings-day",
          "cropBoxPx": [
            70,
            2124,
            1010,
            2268
          ],
          "sourceBoundsPx": [
            78,
            2132,
            1002,
            2260
          ],
          "basis": "Exact #FF612E connected button extent and spec.md settingsLargePositive variant, with 8px context",
          "notes": "17sp label; ordinary capsule reconstruction is compared with original adaptive/smooth shape. Default guest state only.",
          "classification": "derived crop: A original runtime vs D reference runtime",
          "sourceSha256": {
            "original": "209f4bac18cf8c5da8d2eb25263dcad04def3b487a3de02b904bc3ef46d254e3",
            "reference": "f6bc65c3fb97598581853ed2483cdf379d663b3a37d575d246026060167eec11"
          },
          "originalEvidenceId": "ev_627d382d9998bb8838a229e02cf660f5c7aef7aef8c1871f4720c2e113943320",
          "referenceEvidenceId": "ev_086cd80eceb67a3de28f9d1e949e6fdcac174bc534980b326a7eba2c007a7feb",
          "images": {
            "sideBySide": {
              "path": "generated/component-PrimaryButton-side-by-side.png",
              "sha256": "6ee82da059bc923cd19fa55b76a08716331aaaadea8d031e29bbfc053e9f3511",
              "canvasPx": [
                1880,
                144
              ]
            },
            "overlay": {
              "path": "generated/component-PrimaryButton-overlay.png",
              "sha256": "b09ad7dd9ccab08067b5aec4c99c2d9515f8e9029425e12f0bddda1a8f33fdd0",
              "canvasPx": [
                940,
                144
              ]
            },
            "difference": {
              "path": "generated/component-PrimaryButton-difference-x3.png",
              "sha256": "d28027db0c53f779e0296832546be4cd68e0539fb7a66823ae941898398c2a96",
              "canvasPx": [
                940,
                144
              ]
            }
          }
        }
      ]
    }
  ],
  "declaredSourceIdentity": {
    "package": "com.mi.health",
    "versionName": "3.57.0",
    "versionCode": 357000,
    "apkSha256": "2a80fc5a87353c2c6ef3c01fa0085862fe45e4ddb9c2d272cb3780841b5889a6",
    "mineRuntimeVersion": "11.202.21",
    "mineRuntimeSha256": "4f8012fb1ed726d3ef11f068ff7f42b6e74def6059eddaff3426b4dbb7a06958"
  }
};
