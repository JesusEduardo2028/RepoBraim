package com.braim.adapters;

import java.util.Vector;

import com.example.pruebasherlock.R;



import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class ListaAdapterDeezer extends BaseAdapter {

		LayoutInflater layoutInflater;
		private Typeface miLetra;
	
		public static Vector<String> misListas;

		public ListaAdapterDeezer(Activity a) {
			// layoutInflater = (LayoutInflater)
			// a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater = a.getLayoutInflater();
			incializarVector();

		}

		public void incializarVector() {
			// TODO Auto-generated method stub
			misListas = new Vector<String>();
		

			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return misListas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return misListas.elementAt(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
						
			String e = misListas.elementAt(position);

			ImageView imageView; 
			TextView nombreEmocion; 
			View view; 
			if (convertView == null) {
				view = layoutInflater.inflate(R.layout.child_item_deezer_playlist, null);

			} else {
				view = convertView; // Si ya estamos usando una vista creada
				view.setScaleX(1);
				view.setScaleY(1);
			}
			nombreEmocion = (TextView) view.findViewById(R.id.nombreEmocion);
			nombreEmocion.setText(e);
			
			
			
//			miLetra = Typeface.createFromAsset(view.getContext().getAssets(),
//					"DEMO.otf");
//			nombreEmocion.setTypeface(miLetra);

	//		imageView = (ImageView) view.findViewById(R.id.imageView1);
	//		imageView.setImageResource(bookInfo.resourceImage);
	//		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			
//				if (emocion.get(position).equals("nuevo") ){
//				
//				View view_ultimo = layoutInflater.inflate(R.layout.elemento_listas_nuevo, null);
//				return view_ultimo;
//				
//			}else{
			
			return view;
			//}

		}

	}



