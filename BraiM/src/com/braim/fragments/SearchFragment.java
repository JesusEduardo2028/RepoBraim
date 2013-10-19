package com.braim.fragments;

import com.example.pruebasherlock.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class SearchFragment extends Fragment{

	
	private Activity a;
	private View view;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		a = activity;
		super.onAttach(activity);
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
		 view = inflater.inflate(R.layout.layout_fragment_search, container,
				false);
		 
		 
		return view;
	}
	
	
	

}
