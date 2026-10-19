/*! markmap-autoloader v0.13.5 | MIT License */
this.markmap = this.markmap || {};
(function (exports) {
'use strict';

/*! markmap-common v0.13.4 | MIT License */

function _extends() {
  _extends = Object.assign || function (target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];

      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }

    return target;
  };

  return _extends.apply(this, arguments);
}

Math.random().toString(36).slice(2, 8);

function memoize(fn) {
  const cache = {};
  return function memoized(...args) {
    const key = `${args[0]}`;
    let data = cache[key];

    if (!data) {
      data = {
        value: fn(...args)
      };
      cache[key] = data;
    }

    return data.value;
  };
}

function createElement(tagName, props, attrs) {
  const el = document.createElement(tagName);

  if (props) {
    Object.entries(props).forEach(([key, value]) => {
      el[key] = value;
    });
  }

  if (attrs) {
    Object.entries(attrs).forEach(([key, value]) => {
      el.setAttribute(key, value);
    });
  }

  return el;
}

const memoizedPreloadJS = memoize(url => {
  document.head.append(createElement('link', {
    rel: 'preload',
    as: 'script',
    href: url
  }));
});

function loadJSItem(item, context) {
  if (item.type === 'script') {
    return new Promise((resolve, reject) => {
      var _item$data;

      document.head.append(createElement('script', _extends({}, item.data, {
        onload: resolve,
        onerror: reject
      }))); // Run inline script synchronously

      if (!((_item$data = item.data) != null && _item$data.src)) resolve();
    });
  }

  if (item.type === 'iife') {
    const {
      fn,
      getParams
    } = item.data;
    fn(...((getParams == null ? void 0 : getParams(context)) || []));
  }
}

function loadCSSItem(item) {
  if (item.type === 'style') {
    document.head.append(createElement('style', {
      textContent: item.data
    }));
  } else if (item.type === 'stylesheet') {
    document.head.append(createElement('link', _extends({
      rel: 'stylesheet'
    }, item.data)));
  }
}

async function loadJS(items, context) {
  const needPreload = items.filter(item => {
    var _item$data2;

    return item.type === 'script' && ((_item$data2 = item.data) == null ? void 0 : _item$data2.src);
  });
  if (needPreload.length > 1) needPreload.forEach(item => memoizedPreloadJS(item.data.src));
  context = _extends({
    getMarkmap: () => window.markmap
  }, context);

  for (const item of items) {
    await loadJSItem(item, context);
  }
}

function loadCSS(items) {
  for (const item of items) {
    loadCSSItem(item);
  }
}

var _window$markmap, _window$markmap$autoL, _window$markmap2, _window$markmap2$auto;
const enabled = {};
const ready = loadJS(((_window$markmap = window.markmap) == null ? void 0 : (_window$markmap$autoL = _window$markmap.autoLoader) == null ? void 0 : _window$markmap$autoL.baseJs) || [{
  type: 'script',
  data: {
    src: `./npm/d3-6.7.0/dist/d3.js`
  }
}, {
  type: 'script',
  data: {
    src: `npm/markmap-lib-0.13.5/dist/index.js`
  }
},{
    type: 'script',
    data: {
      src: `npm/markmap-view-0.13.5/dist/browser/index.js`
    }
  }]).then(() => {
  var _markmap$autoLoader;

  const {
    markmap
  } = window;
  loadCSS([{
    type: 'style',
    data: markmap.globalCSS
  }]);
  (_markmap$autoLoader = markmap.autoLoader) == null ? void 0 : _markmap$autoLoader.onReady == null ? void 0 : _markmap$autoLoader.onReady();
});

function transform(transformer, content) {
  const {
    root,
    features
  } = transformer.transform(content);
  const keys = Object.keys(features).filter(key => !enabled[key]);
  keys.forEach(key => {
    enabled[key] = true;
  });
  const {
    styles,
    scripts
  } = transformer.getAssets(keys);
  const {
    markmap
  } = window;
  if (styles) markmap.loadCSS(styles);
  if (scripts) markmap.loadJS(scripts);
  return root;
}

function render(el) {
  const {
    Transformer,
    Markmap,
    autoLoader
  } = window.markmap;
  const lines = el.textContent.split('\n');
  let indent = Infinity;
  lines.forEach(line => {
    const spaces = line.match(/^\s*/)[0].length;
    if (spaces < line.length) indent = Math.min(indent, spaces);
  });
  const content = lines.map(line => line.slice(indent)).join('\n');
  const transformer = new Transformer(autoLoader == null ? void 0 : autoLoader.transformPlugins);
  el.innerHTML = '<svg></svg>';
  const svg = el.firstChild;
  const mm = Markmap.create(svg, {
    embedGlobalCSS: false,
    initialExpandLevel: (autoLoader == null ? -1 : autoLoader.initialExpandLevel),
    fitMaxLength: (autoLoader == null ? false : autoLoader.fitMaxLength),
    fitRatio: (autoLoader == null ? 0.95 : autoLoader.fitRatio),
  });

  const doRender = () => {
    const root = transform(transformer, content);
    mm.setData(root);

    console.log('rendered markmap data:', root.content);
    mm.fit().then(() => {
        // 动画完成后的回调
        if (autoLoader?.onRenderComplete) {
           autoLoader.onRenderComplete(root);
        }
        // 可以在这里添加其他完成后的逻辑
      }).catch(() => {
        // 动画被中断时的处理（可选）
        if (autoLoader?.onRenderComplete) {
           autoLoader.onRenderComplete(null);
        }
      });
  };

  transformer.hooks.retransform.tap(doRender);
  doRender();

  // 修复：markmap 渲染后仅在首屏 fit 一次。当 WebView 视口宽度/缩放尚未 settle 时
  // （如内联预览首帧、HorizontalPager 首次布局），fit 会按错误尺寸计算，导致脑图
  // 贴在顶部、缩放错位，需手动点刷新才正常。这里监听 svg 容器尺寸变化，待尺寸真正
  // 变化后防抖重新 fit，自动纠正首屏错位（恢复官方 markmap 的 resize 自愈行为）。
  let _refitTimer = null;
  const scheduleRefit = () => {
    if (_refitTimer) clearTimeout(_refitTimer);
    _refitTimer = setTimeout(() => {
      _refitTimer = null;
      mm.fit();
    }, 120);
  };
  if (typeof ResizeObserver !== 'undefined') {
    let lastW = svg.clientWidth;
    let lastH = svg.clientHeight;
    const ro = new ResizeObserver(() => {
      const w = svg.clientWidth;
      const h = svg.clientHeight;
      // 仅在尺寸真正变化时重新 fit，避免初始回调、以及截图等稳定尺寸场景下多余的 fit
      if (Math.abs(w - lastW) > 1 || Math.abs(h - lastH) > 1) {
        lastW = w;
        lastH = h;
        scheduleRefit();
      }
    });
    ro.observe(svg);
  } else {
    window.addEventListener('resize', scheduleRefit);
  }
}
async function renderAllUnder(container) {
  await ready;
  container.querySelectorAll('.markmap').forEach(render);
}
function renderAll() {
  return renderAllUnder(document);
}

if (!((_window$markmap2 = window.markmap) != null && (_window$markmap2$auto = _window$markmap2.autoLoader) != null && _window$markmap2$auto.manual)) {
  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', () => {
    renderAll();
  });else renderAll();
}

exports.ready = ready;
exports.render = render;
exports.renderAll = renderAll;
exports.renderAllUnder = renderAllUnder;

})(this.markmap.autoLoader = this.markmap.autoLoader || {});
