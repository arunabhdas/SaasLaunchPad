const blurFilter = "blur(6px)"

let textToBlur = ""



// Search this DOM node for text to blur and blur the parent element if found
function processNode(node: Node) {
    if (node.childNodes.length > 0) {
        Array.from(node.childNodes).forEach(processNode)
    }

    if (node.nodeType === Node.TEXT_NODE
        && node.textContent !== null && node.textContent.trim().length > 0) {
        const parent = node.parentElement
        if (parent == null) {
            return
        }
        if (parent.tagName == 'SCRIPT' || parent.style.filter == blurFilter) {
            // Already blurred
            return
        }

        if (node.textContent.includes(textToBlur)) {
            blurElement(parent)
        }
    }
}

function blurElement(elem: HTMLElement) {
    elem.style.filter = blurFilter
    console.debug("blurred id:" + elem.id + " class:" + elem.className +
        " tag: " + elem.tagName + " text:" + elem.textContent)
}

// Create a MutationObserver to watchfor changes to the DOM
const observer = new MutationObserver( (mutations) => {
        mutations.forEach( (mutation) => {
           if (mutation.addedNodes.length > 0) {
               mutation.addedNodes.forEach(processNode)
           } else {
               processNode(mutation.target)
           }
        })
})

// Enable the content script by default
let enabled = true
const keys = ["enabled", "item"]

function observe() {
    // Only start observing the DOM if the extension is enabled and there is text to blur
    if (enabled && textToBlur.trim().length > 0) {
        observer.observe(document, {
            attributes: false,
            characterData: true,
            childList: true,
            subtree: true,
        })
        // Loop through all elements on the page for initial processing
        processNode(document)
    }
}

chrome.storage.sync.get(keys, (data) => {
    if (data.enabled === false) {
        enabled = false
    }
    if (data.item) {
        textToBlur = data.item
    }
    observe()
})

// Listen for messages from popup
chrome.runtime.onMessage.addListener( (request, sender, sendResponse) => {
    if (request.enabled !== undefined) {
        console.log("Received message from sender %s", sender.id, request)
        enabled = request.enabled
        if (enabled) {
            observe()
        } else {
            observer.disconnect()
        }
        sendResponse({title: document.title, url: window.location.href})
    }

    // Handle blurNow action from popup
    if (request.action === "blurNow") {
        console.log("Received blurNow request with text:", request.textToBlur)
        
        // Store the original text to blur
        const originalText = textToBlur
        
        // Temporarily set the text to blur to the requested text
        textToBlur = request.textToBlur
        
        // Disconnect observer temporarily to avoid double processing
        observer.disconnect()
        
        // Track how many elements were blurred
        let blurredCount = 0
        
        // Create a wrapper function for processNode that counts blurred elements
        /*
        function processNodeAndCount(node: Node) {
            if (node.childNodes.length > 0) {
                Array.from(node.childNodes).forEach(processNodeAndCount)
            }

            if (node.nodeType === Node.TEXT_NODE
                && node.textContent !== null && node.textContent.trim().length > 0) {
                const parent = node.parentElement
                if (parent == null) {
                    return
                }
                if (parent.tagName == 'SCRIPT' || parent.style.filter == blurFilter) {
                    return
                }

                if (node.textContent.includes(textToBlur)) {
                    blurElement(parent)
                    blurredCount++
                }
            }
        }
        processNodeAndCount(document)
        */
        processNode(document)
        
        // Restore the original text to blur
        textToBlur = originalText
        
        // Reconnect observer if extension is enabled
        if (enabled && textToBlur.trim().length > 0) {
            observe()
        }
        
        // Send response with the number of elements blurred
        sendResponse({
            success: true,
            count: blurredCount
        })
        
        return true // Keep the message channel open for the async response
    }

    return true // Keep the message channel open for the async response

})
