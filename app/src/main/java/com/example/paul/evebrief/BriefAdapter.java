package com.example.paul.evebrief;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Paul on 13/11/2014.
 */
public class BriefAdapter extends BaseAdapter {
    Context context;
    ArrayList<Brief> briefs;
    private static LayoutInflater inflater = null;

    public BriefAdapter(Context context, DisplayedBriefs briefs) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.briefs = briefs.getBriefs();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return briefs.size();
    }

    @Override
    public Object getItem(int position) {
        return briefs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.eve_list_item, null);

        Brief brief = briefs.get(position);

        TextView text = (TextView) vi.findViewById(R.id.title);
        text.setText(brief.getTitle());
        text.setTextColor(Color.parseColor("#000000"));

        TextView text2 = (TextView) vi.findViewById(R.id.date);
        text2.setText(brief.getDate());
        text2.setTextColor(Color.parseColor("#000000"));

        ImageView imageView= (ImageView) vi.findViewById(R.id.image);
        imageView.setImageBitmap(brief.getImage());


        return vi;
    }

    public void setBriefs(DisplayedBriefs briefs) {
        this.briefs = briefs.getBriefs();
    }


}
