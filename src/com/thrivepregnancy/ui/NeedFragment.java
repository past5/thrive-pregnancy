package com.thrivepregnancy.ui;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.thrivepregnancy.R;
import com.thrivepregnancy.data.EventDataHelper;
import com.thrivepregnancy.data.Need;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;

public class NeedFragment extends Fragment {
	ListView list;

	private EventDataHelper 	dataHelper = null;
	private Dao<Need, Integer> needDao;
	  
	/**
	 * Empty public constructor required per the {@link Fragment} API documentation
	 */
	public NeedFragment(){}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_need, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (dataHelper == null) {
			MainActivity activity = (MainActivity)getActivity();		
	    	dataHelper = new EventDataHelper(activity.getHelper());
	    	needDao = dataHelper.getNeedDao();
	    }
	    
	    NeedAdapter adapter = new NeedAdapter(getView().getContext());
	    
	    list=(ListView)getView().findViewById(R.id.lst_need);
	    list.setAdapter(adapter);
	    
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	@Override
	    	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    	}
	    });
	}
	
  	private class NeedAdapter extends BaseAdapter{
		private Context context;
		
		public NeedAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			View item = null;
			final Need need = dataHelper.getNeeds().get(position);
			final String needTitle = need.getTitle();
			
			item = LayoutInflater.from(context).inflate(R.layout.need_list, parent, false);
			
			TextView txtNeed = (TextView) item.findViewById(R.id.txt_needtitle);
			txtNeed.setText(needTitle);
			
			RadioButton radioNeedIt = (RadioButton) item.findViewById(R.id.radio_needit);
			RadioButton radioGotIt = (RadioButton) item.findViewById(R.id.radio_gotit);
			
			if (need.getNeedit()) radioNeedIt.setChecked(true);
			if (need.getGotit()) radioGotIt.setChecked(true);
			
			ImageButton resource = (ImageButton) item.findViewById(R.id.img_needresource);
			
			resource.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), InformationActivity.class);
					Bundle parentBundle = new Bundle();
					String htmlfile = need.getResources();
					
					if (htmlfile == null || htmlfile.length() == 0) htmlfile = "empty.html";
					
					parentBundle.putString("htmlfile", htmlfile);
					parentBundle.putString("needtitle", needTitle);
					
					intent.putExtras(parentBundle);
					startActivity(intent);
				}
			});
			
			radioNeedIt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RadioButton current = (RadioButton) v;
					
					boolean checked = current.isChecked();
					
					try {
						if (checked) {
							need.setNeedit(true);
							need.setGotit(false);
						} else {
							need.setNeedit(false);
							need.setGotit(true);
						}
						
						needDao.update(need);
					} catch (SQLException e) {
						//Log.e(NeedAdapter.class.getName(), "Unable to set need it flag", e);
					}
				}
			});
			
			radioGotIt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RadioButton current = (RadioButton) v;
					
					boolean checked = current.isChecked();
					
					try {
						if (checked) {
							need.setGotit(true);
							need.setNeedit(false);
						} else {
							need.setGotit(false);
							need.setNeedit(true);
						}
						
						needDao.update(need);
					} catch (SQLException e) {
						//Log.e(NeedAdapter.class.getName(), "Unable to set got it flag", e);
					}
				}
			});
			
			
			return item;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataHelper.getNeeds().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}