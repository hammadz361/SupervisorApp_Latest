package pk.clsurvey.gb.supervision;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ehtisham Ullah on 3/6/2018.
 */
public class custom_adapter extends ArrayAdapter<status> {
//    public class ListAdapter extends ArrayAdapter<Item> {

        public custom_adapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public custom_adapter(Context context, int resource, ArrayList<status> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_rows, null);
            }

            status p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.hh_id);
                TextView tt2 = (TextView) v.findViewById(R.id.status);
                TextView tt3 = (TextView) v.findViewById(R.id.result);
                TextView tt4   = (TextView) v.findViewById(R.id.date);
                TextView tt5   = (TextView) v.findViewById(R.id.revisit);


                if (tt1 != null) {
                    tt1.setText(p.getHH_IS());
                }

                if (tt2 != null) {
                    tt2.setText(p.getStatus_HH());
                }
                if (tt4 != null) {
                    if(p.getTimestamp().contains("null") || p.getTimestamp().contains("NULL") || p.getTimestamp().length()==0)
                     tt4.setText("");
                    else

                        tt4.setText("Submitted @ "+p.getTimestamp());
                }
                if (tt4 != null) {
                    if(p.getRevisit().contains("null") || p.getRevisit().contains("NULL") || p.getRevisit().length()==0)
                    {
                        tt5.setText("");

                    }
                    else{
                        tt5.setText("Revisit Planned @ " + p.getRevisit());
                    }
               //   if(p.getRevisit().equals(" "))
                //       tt5.setVisibility(View.INVISIBLE);
                 // if(p.getTimestamp().equals(" "))
                  //    tt4.setVisibility(View.INVISIBLE);
                }
                if (tt3 != null) {
                    if(p.getResult_HH().equals("null") || p.getResult_HH().equals("") ) {
                        tt3.setText("------");
                    }
                    else
                    tt3.setText(p.getResult_HH());
                }
                Button change = (Button) v.findViewById(R.id.change);
                if(!p.getSampled().isEmpty()) {
                    if (p.getSampled().contains("True") || p.getSampled().contains("true")) {
                        v.setBackgroundColor(Color.rgb(	1, 50, 32));
                        change.setEnabled(true);
//                        tt1.setTypeface(null, Typeface.BOLD);
                    }
                     if (p.getSampled().contains("False") || p.getSampled().contains("false")) {
                        v.setBackgroundColor(Color.rgb(	170, 169, 32));
//                        tt1.setTypeface(null, Typeface.NORMAL);
                        change.setEnabled(false);
                    }
                     if(p.getSampled().contains("Excluded"))
                         {
                        v.setBackgroundColor(Color.rgb(	170, 32, 33));
//                        tt1.setTypeface(null, Typeface.NORMAL);
                        change.setEnabled(false);
                    }
                    return v;
                }

            }

            return v;
        }

    }
//}
//    private final Activity context;
//
//    private ArrayList<status> Items;
//    public custom_adapter(Activity context, int layout, ArrayList<status> status_obj) {
//        super(context, layout);
//        // super(context, layout, carriers);
//        // TODO Auto-generated constructor stub
//
//        this.context = context;
//        this.Items = status_obj;
//
//    }
//
//    static class ViewHolder {
//        public TextView hh_id;
//        public TextView status_hh;
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
//        // Recycle existing view if passed as parameter
//        // This will save memory and time on Android
//        // This only works if the base layout for all classes are the same
//        View rowView = convertView;
//        if (rowView == null) {
//            LayoutInflater inflater = context.getLayoutInflater();
//            rowView = inflater.inflate(R.layout.list_rows, null, false);
//
//            holder = new ViewHolder();
//            holder.hh_id= (TextView) rowView.findViewById(R.id.hh_id);
//            holder.status_hh= (TextView) rowView.findViewById(R.id.status);
//            rowView.setTag(holder);
//        } else {
//            holder = (ViewHolder) rowView.getTag();
//        }
//
//        holder.hh_id.setText(Items.get(position).getHH_IS());
//        holder.status_hh.setText(Items.get(position).getStatus_HH());
//
//
//        return rowView;
//
//    }
//
//}