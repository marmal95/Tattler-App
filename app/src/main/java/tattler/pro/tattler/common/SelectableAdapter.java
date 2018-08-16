package tattler.pro.tattler.common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder, SelectableItem> extends RecyclerView.Adapter<VH> {
    private SparseBooleanArray selectedItems;
    private boolean isInSelectMode;

    public SelectableAdapter() {
        selectedItems = new SparseBooleanArray();
        isInSelectMode = false;
    }

    public boolean isSelected(int position) {
        return selectedItems.indexOfKey(position) >= 0;
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }

        isInSelectMode = getSelectedItemsCount() > 0;
        notifyItemChanged(position);
    }

    public void clearSelection() {
        selectedItems.clear();
        isInSelectMode = false;
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedPositions() {
        ArrayList<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public abstract List<SelectableItem> getSelectedItems();

    public boolean isInSelectMode() {
        return isInSelectMode;
    }
}
