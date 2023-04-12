package thud.mymusicapp.interfaces.album;

import static thud.mymusicapp.interfaces.MainActivity.userid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import thud.mymusicapp.processing.AlbumAdapter;
import thud.mymusicapp.processing.MusicAdapter;
import thud.mymusicapp.R;
import thud.mymusicapp.data.Album;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    MusicAdapter musicAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        musicAdapter = new MusicAdapter(getContext());
        albumAdapter = new AlbumAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        albumAdapter = new AlbumAdapter(getContext());
        ArrayList<Album> albums = new ArrayList<>();
        // list album cua 1 user nao do
        // do album user la dung chung nen neu la user dau tien thi  list binh thuong
        // neu != user 1 lay album dau tien la album device sau do lay cac album con lai cua user do
        if (userid == 1)
        {
            albums = albumAdapter.ListAllAlbum(userid);
        } else {
            Album temp = albumAdapter.ListAllAlbum().get(0);
            albums.add(temp);
            ArrayList<Album> tempAlbumList = albumAdapter.ListAllAlbum(userid);
            for (int i = 0; i < tempAlbumList.size(); i++)
            {
                albums.add(tempAlbumList.get(i));
            }
        }
        if (!(albums.size() < 1))
        {
            albumAdapter = new AlbumAdapter(getContext(), albums);
            recyclerView.setAdapter(albumAdapter);
            // tra ve Gridlayout 2 cot
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}