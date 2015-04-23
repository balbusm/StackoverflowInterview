package com.polidea.stackoverflowinterview.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polidea.stackoverflowinterview.R;
import com.polidea.stackoverflowinterview.domain.Owner;
import com.polidea.stackoverflowinterview.domain.Summary;

import java.util.List;

public class ResultAdapter extends BaseAdapter {

    private final Context context;
    private final List<Summary> summaries;
    private final LayoutInflater inflater;

    public ResultAdapter(Context context, List<Summary> summaries) {
        this.context = context;
        this.summaries = summaries;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return summaries.size();
    }

    @Override
    public Summary getItem(int i) {
        return summaries.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.result, null);
            final ImageView avatarView = (ImageView) convertView.findViewById(R.id.avatar_id);
            final TextView authorNameView = (TextView) convertView.findViewById(R.id.author_name_id);
            final TextView answersView = (TextView) convertView.findViewById(R.id.answers_id);
            final TextView titleView = (TextView) convertView.findViewById(R.id.title_id);

            viewHolder = new ViewHolder(avatarView, authorNameView, answersView, titleView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        fillView(viewHolder, getItem(i));

        return convertView;
    }

    private void fillView(ViewHolder viewHolder, Summary summary) {
        final Owner owner = summary.getOwner();
        setImage(viewHolder.avatarView, owner.getProfileImage());
        viewHolder.authorNameView.setText(owner.getDisplayName());
        viewHolder.titleView.setText(summary.getTitle());
        viewHolder.answersView.setText(Integer.toString(summary.getAnswersCount()));

    }

    private void setImage(ImageView imageVIew, Bitmap bitmap) {
        if (bitmap == null) {
            imageVIew.setImageResource(R.drawable.ic_launcher);
        } else {
            imageVIew.setImageBitmap(bitmap);
        }
    }

    private static class ViewHolder {

        private final ImageView avatarView;
        private final TextView authorNameView;
        private final TextView answersView;
        private final TextView titleView;

        public ViewHolder(ImageView avatarView, TextView authorNameView, TextView answersView, TextView titleView) {

            this.avatarView = avatarView;
            this.authorNameView = authorNameView;
            this.answersView = answersView;
            this.titleView = titleView;
        }
    }

}
