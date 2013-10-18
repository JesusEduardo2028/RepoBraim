package com.braim.fragments;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;

import com.braim.MainActivity;
import com.braim.ReproductorActivity;
import com.braim.deezer.data.Playlist;
import com.braim.relist.RefreshableListView;
import com.braim.relist.RefreshableListView.OnPullUpUpdateTask;
import com.braim.relist.RefreshableListView.OnUpdateTask;
import com.example.pruebasherlock.R;

public class RecomendadorFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener{
	
	private Activity actividad;
	private View view;
	private RefreshableListView listaDeslizable;
	private long  playlistId = 4341978;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actividad = activity;
	
		try {
		
	
		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()
					+ " ha de implementar onGridViewListener");
		}
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view != null){
			ViewGroup parent = (ViewGroup)view.getParent();
			if (parent != null){
				parent.removeView(view);
			}
		}
		 view = inflater.inflate(R.layout.layout_fragment_recomendador, container,
				false);
		 final ArrayList<String> listaCanciones = new ArrayList<String>();
		 for(int i=0;i<10;i++){
			 listaCanciones.add("cancion " +i);
			 
		 }
		 final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,listaCanciones);
		 
		 final RefreshableListView list = (RefreshableListView) view.findViewById(R.id.refreshablelistview);
		 listaDeslizable = list;
		 list.setAdapter(MainActivity.arrayAdapterTrack);

		 list.setOnUpdateTask(new OnUpdateTask() {
			
			@Override
			public void updateUI() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void updateBackground() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onUpdateStart() {
				// TODO Auto-generated method stub
				
			}
		});
		 
		 list.setOnPullUpUpdateTask(new OnPullUpUpdateTask() {

				@Override
				public void onUpdateStart() {

				}

				@Override
				public void updateBackground() {
					// simulate long times operation.
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void updateUI() {
					listaCanciones.add("list item" + listaCanciones.size());
					adapter.notifyDataSetChanged();
				}

			});
		 list.setOnItemClickListener(new OnItemClickListener() {
	
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int listId,
						long arg3) {
					// TODO Auto-generated method stub
					
					Intent intent  = new Intent(getActivity(), ReproductorActivity.class);
					intent.putExtra(ReproductorActivity.PLAYLIST_ID, playlistId);
					intent.putExtra(ReproductorActivity.PLAYLIST_TITLE, "Mis Recomendaciones");
					intent.putExtra("posicion", listId-1);
					intent.putExtra("user", MainActivity.nombre_user+" "+MainActivity.apellido_user);
				//	intent.putExtra("user", MainActivity.user_id);
					startActivity(intent);
					
				//	Toast.makeText(getActivity(),a.getTitle() + " presionada", Toast.LENGTH_SHORT).show();
					
				}
		});
		 
		return view;
		
		}

	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
