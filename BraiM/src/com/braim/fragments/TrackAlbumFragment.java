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
import android.widget.ListView;
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

public class TrackAlbumFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener{
	
	private Activity actividad;
	private View view;
	private RefreshableListView listaDeslizable;
	private long  playlistId = 4341978;
	private ListView list;
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
		final long albumID = getArguments().getLong("albumID");
		final String albumTittle = getArguments().getString("albumTittle");
		final String albumCover = getArguments().getString("albumUrl");
		if (view != null){
			ViewGroup parent = (ViewGroup)view.getParent();
			if (parent != null){
				parent.removeView(view);
			}
		}
		 view = inflater.inflate(R.layout.layout_fragment_album_track, container,
				false);
		 final ArrayList<String> listaCanciones = new ArrayList<String>();
		 for(int i=0;i<10;i++){
			 listaCanciones.add("cancion " +i);
			 
		 }
		 final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,listaCanciones);
		 
		 list = (ListView) view.findViewById(R.id.listView1);
	
		 list.setAdapter(MainActivity.arrayAdapterTrack);
		 
		 list.setOnItemClickListener(new OnItemClickListener() {
	
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int listId,
						long arg3) {
					// TODO Auto-generated method stub
					
			
					
					Intent intent  = new Intent(getActivity(), ReproductorActivity.class);
					intent.putExtra(ReproductorActivity.PLAYLIST_ID, -1);
					intent.putExtra("albumTittle", albumTittle);
					intent.putExtra("albumID", albumID);
					intent.putExtra("albumUrl", albumCover);
					intent.putExtra("user", MainActivity.nombre_user+" "+MainActivity.apellido_user);
					intent.putExtra("posicion", listId);
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
