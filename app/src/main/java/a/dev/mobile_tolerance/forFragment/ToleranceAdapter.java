package a.dev.mobile_tolerance.forFragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

import java.util.List;
import java.util.Locale;

import a.dev.mobile_tolerance.R;

class ToleranceAdapter extends BaseTableAdapter {
    private final FragmentActivity activity;
    private final List<ToleranceItem> toleranceItemList;
    private final float density;

    private final int ROW_COUNT;
    private final int COLUMN_COUNT;

    Configuration config;

    public ToleranceAdapter(FragmentActivity activity, List<ToleranceItem> toleranceItemList) {
        config = new Configuration();
        this.activity = activity;
        this.toleranceItemList = toleranceItemList;
        this.density = activity.getResources().getDisplayMetrics().density;
        this.ROW_COUNT = toleranceItemList.get(0).values.length + 2;
        //-2 т.к. пропускаем 2 колонки
        this.COLUMN_COUNT = toleranceItemList.size() - 1;
    }

    @Override
    public int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        final View view;
        switch (getItemViewType(row, column)) {
            case 0:
                view = getFirstHeader(convertView, parent);
                break;
            case 1:
                view = getHeader(column, convertView, parent);
                break;
            case 2:
                view = getFirstBody(row, convertView, parent);
                break;
            case 3:
                view = getBody(row, column, convertView, parent);
                break;
            case 4:
                view = getFamilyView(column, convertView, parent);
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return view;
    }

    @Override
    public int getWidth(int column) {
        if (column == -1) {
            float WIDTH_FIRST_COLUMN = 85;
            return Math.round(WIDTH_FIRST_COLUMN * density);
        } else {
            float WIDTH_OTHER_COLUMN = 70;
            return Math.round(WIDTH_OTHER_COLUMN * density);
        }


    }

    @Override
    public int getHeight(int row) {
        final int height;
        if (row == -1) {
            height = 35;
        } else if (isFamily(row)) {
            height = 10;
        } else {
            height = 45;
        }
        return Math.round(height * density);
    }

    @Override
    public int getItemViewType(int row, int column) {
        final int itemViewType;
        if (row == -1 && column == -1) {
            itemViewType = 0;
        } else if (row == -1) {
            itemViewType = 1;
        } else if (isFamily(row)) {
            itemViewType = 4;
        } else if (column == -1) {
            itemViewType = 2;
        } else {
            itemViewType = 3;
        }
        return itemViewType;
    }

    private boolean isFamily(int row) {
        int family = 0;
        while (row > 0) {
            row -= toleranceItemList.get(family).values.length + 1;
            family++;
        }
        return row == 0;
    }

    private String textFromSQL(String description) {


        if (config.locale == Locale.ENGLISH) {
            description = description.replaceAll("от", "");
        }
        return description.replaceAll("\\\\n", "\\\n").replaceAll("\\\\t", "\\\t").replaceAll("_", "");
    }

    private View getFamilyView(int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_family, parent, false);
        }
        final String string;
        if (column == -1) {
            string = "";
        } else {
            string = "";
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return 5;
    }

    private View getFirstHeader(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_header_first, parent, false);
        }
        TextView firstHeader = ((TextView) convertView.findViewById(android.R.id.text1));
        firstHeader.setTextColor(Color.BLACK);
        firstHeader.setTypeface(firstHeader.getTypeface(), Typeface.BOLD);
        firstHeader.setText(textFromSQL(toleranceItemList.get(0).headerColumn));

        return convertView;
    }

    private View getHeader(int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_header, parent, false);
        }

        TextView header = ((TextView) convertView.findViewById(android.R.id.text1));
        header.setTextColor(Color.BLACK);
        header.setTypeface(header.getTypeface(), Typeface.BOLD);
        header.setText(textFromSQL(toleranceItemList.get(column + 1).headerColumn));


        return convertView;
    }

    private View getFirstBody(int row, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_first, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.color.background_row_1 : R.color.background_row_2);


        TextView firstColumn = ((TextView) convertView.findViewById(android.R.id.text1));
        firstColumn.setTextColor(Color.BLACK);

        firstColumn.setText(textFromSQL(toleranceItemList.get(0).values[row - 1]));


        return convertView;
    }

    private View getBody(final int row, final int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.color.background_row_1 : R.color.background_row_2);
        TextView header = ((TextView) convertView.findViewById(android.R.id.text1));
        header.setTextColor(Color.BLACK);
        final String tolerance = textFromSQL(toleranceItemList.get(column + 1).values[row - 1]);
        header.setText(tolerance);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ttt", tolerance+"\n"+"row = " + row + "\n" + "column = " + column);
            }
        });
        return convertView;
    }


}
