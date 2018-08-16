package tattler.pro.tattler.main.contacts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.common.SelectableAdapter;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Contact;

public class ContactsAdapter extends SelectableAdapter<ContactsAdapter.ViewHolder, Contact> {
    private Context context;
    private List<Contact> contacts;
    private OnItemClickListener clickListener;
    private int lastPosition;

    ContactsAdapter(Context context, List<Contact> contacts, OnItemClickListener clickListener) {
        this.context = context;
        this.contacts = contacts;
        this.clickListener = clickListener;
        this.lastPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = getContact(position);
        holder.contactNumber.setText(String.valueOf(contact.contactNumber));
        holder.userName.setText(contact.contactName);
        holder.userAvatar.setColorFilter(Color.parseColor(Util.pickHexColor(context, contact.contactName)));

        if (isSelected(position)) {
            holder.userInitials.setText("");
            holder.userAvatar.setImageDrawable(VectorDrawableCompat.create(context.getResources(),
                    R.drawable.ic_check_circle, context.getTheme()));
        } else {
            ColorDrawable userNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(context, contact.contactName)));
            holder.userInitials.setText(Util.extractUserInitials(contact.contactName));
            holder.userAvatar.setImageDrawable(userNameColorDrawable);
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public List<Contact> getSelectedItems() {
        List<Contact> selectedContacts = new ArrayList<>(getSelectedItemsCount());
        getSelectedPositions().forEach(index -> selectedContacts.add(getContact(index)));
        return selectedContacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        notifyDataSetChanged();
    }

    public void addContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    public void clearContacts() {
        contacts.clear();
        notifyDataSetChanged();
    }

    public void removeContact(Contact contact) {
        OptionalInt contactPosition = IntStream.range(0, contacts.size()).filter(
                index -> contact.contactNumber == getContact(index).contactNumber).findFirst();
        if (contactPosition.isPresent()) {
            contacts.remove(contactPosition.getAsInt());
            notifyItemRemoved(contactPosition.getAsInt());
        }
    }

    public void removeContact(int position) {
        contacts.remove(position);
        notifyItemRemoved(position);
    }

    public Contact getContact(int position) {
        return contacts.get(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userAvatar)
        CircleImageView userAvatar;

        @BindView(R.id.userInitials)
        TextView userInitials;

        @BindView(R.id.userName)
        TextView userName;

        @BindView(R.id.userNumber)
        TextView contactNumber;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> clickListener.onItemClick(getAdapterPosition()));
            itemView.setOnLongClickListener(v -> clickListener.onItemLongClick(getAdapterPosition()));
        }
    }
}
