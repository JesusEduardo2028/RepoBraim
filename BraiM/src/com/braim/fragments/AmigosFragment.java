package com.braim.fragments;

import com.braim.MainActivity;
import com.braim.deezer.data.User;
import com.example.pruebasherlock.R;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AmigosFragment extends Fragment {

	
	private Activity actividad;
	private ArrayAdapter<User> arrayAdapter;
	private View view;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actividad = activity;
	
		try {
		//	mCallback = (myinterfaz) activity;
	
		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()
					+ " ha de implementar onListViewListener");
		}
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			arrayAdapter = MainActivity.arrayAdapterUsers;
//			Toast.makeText(getActivity(), " ADAPTER", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				Toast.makeText(getActivity(), "ERROR ADAPTER", Toast.LENGTH_SHORT).show();
			}
		if (view != null){
			ViewGroup parent = container;
			if (parent != null)
				parent.removeView(view);
			
		}
		 view = inflater.inflate(R.layout.layout_fragment_amigos, container,
				false);

		ListView listaAmigos = (ListView) view.findViewById(R.id.listView1);
		listaAmigos.setAdapter(arrayAdapter);
		
		return view;
		
		}

	
	
	
	

}
