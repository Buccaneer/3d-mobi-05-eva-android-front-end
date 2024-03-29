package be.evavzw.eva21daychallenge.models.profile_setup;

import java.util.ArrayList;

/**
 * Represents a node in the page tree. Can either be a single page, or a page container.
 */
public interface PageTreeNode {
    public Page findByKey(String key);

    public void flattenCurrentPageSequence(ArrayList<Page> dest);
}
