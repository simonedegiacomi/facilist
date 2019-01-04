/**
 * Common array utils
 */


/**
 * Remove an item from the array if it's present
 * @param array
 * @param item Item to remove
 */
export function removeFromArrayIfPresent(array, item) {
    const index = array.indexOf(item);

    if (index >= 0) {
        array.splice(index, 1);
    }
}

/**
 * Remove an item, given it's id, in the array if present
 * @param array
 * @param id Id of the item to remove
 */
export function removeFromArrayByIdIfPresent(array, id) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1);
    }
}

/**
 * Replace an item, given it's id, if it's present
 * @param array
 * @param id Id of the item to replace
 * @param newItem New item
 */
export function replaceArrayItemByIdIfPresent(array, id, newItem) {
    const index = array.findIndex(c => c.id == id);

    if (index >= 0) {
        array.splice(index, 1, newItem);
    }
}

