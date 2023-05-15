package com.example.myhomework1.Fragments;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.myhomework1.Interfaces.CallBack_SendClick;
import com.example.myhomework1.Models.Record;
import com.example.myhomework1.R;
import com.example.myhomework1.Utilities.DataManager;

public class ListFragment extends Fragment {
    private ListView listView;
    LinearLayout list_item_LL_record;

    private ArrayAdapter<Record> adapter;
    private CallBack_SendClick callBack_SendClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        adapter = new ArrayAdapter<Record>(getContext(), R.layout.list_item_layout, R.id.list_item_name, DataManager.getRecordsList().getList()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView nameTextView = view.findViewById(R.id.list_item_name);
                TextView scoreTextView = view.findViewById(R.id.list_item_score);
                Record record = getItem(position);
                nameTextView.setText((position+1)+". "+record.getName());
                scoreTextView.setText("\t\t " + record.getScore());
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                Record record = adapter.getItem(position);
                if (record != null && callBack_SendClick != null) {
                    callBack_SendClick.userRecordClicked(record);
                }
            }
        });
    }
    private void sendClicked() {
        int position = listView.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            Record record = DataManager.getRecordsList().getList().get(position);
            if(callBack_SendClick != null) {
                callBack_SendClick.userRecordClicked(record);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public void setCallBack(CallBack_SendClick callBack_SendClick) {
        this.callBack_SendClick = callBack_SendClick;
    }
    private void findViews(View view) {
        listView = view.findViewById(R.id.records_LISTVIEW_list);
        list_item_LL_record = view.findViewById(R.id.list_item_LL_record);
    }
}