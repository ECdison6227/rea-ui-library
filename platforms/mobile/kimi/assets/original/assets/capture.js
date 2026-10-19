
import { snapdom } from "./snapdom.min.mjs"; // Ensure this path is correct

// Export a function that takes the target element or selector as an argument
export async function triggerCapture(targetElementOrSelector, bgColor) {
    console.log("Initializing capture for:", targetElementOrSelector);

    const el = (typeof targetElementOrSelector === 'string')
        ? document.querySelector(targetElementOrSelector)
        : targetElementOrSelector;

    if (!el) {
        console.log("triggerCaptureFailed:Capture element not found with selector/object:", targetElementOrSelector);
        return;
    }

    console.log("Element to capture:", el);

    try {
        const rr = await snapdom(el, { scale: 1.5 });
        const img = await rr.toJpg({backgroundColor: bgColor}); // Or toPng(), etc.
        _Capture?.onCapture(img.src)

    } catch (error) {
        console.error("triggerCaptureFailed: Error capturing img",error);
    }
}

