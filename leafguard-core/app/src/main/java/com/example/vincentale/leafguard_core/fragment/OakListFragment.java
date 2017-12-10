package com.example.vincentale.leafguard_core.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vincentale.leafguard_core.R;
import com.example.vincentale.leafguard_core.model.Oak;
import com.example.vincentale.leafguard_core.model.OakManager;
import com.example.vincentale.leafguard_core.model.User;
import com.example.vincentale.leafguard_core.model.UserManager;
import com.example.vincentale.leafguard_core.view.OakAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OakListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OakListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OakListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private List<Oak> oakList = new ArrayList<>();

    private UserManager userManager;
    private OakManager oakManager;
    private User user;

    public OakListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OakListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OakListFragment newInstance() {
        OakListFragment fragment = new OakListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = UserManager.getInstance();
        oakManager = OakManager.getInstance();
        user = userManager.getUser();

        if (user.getOak() != null) {
            oakList.add(user.getOak());
        }
        oakList.add(new Oak(user, (float)Math.random(), (float)Math.random()));
        oakList.add(new Oak(user, (float)Math.random(), (float)Math.random()));
        oakList.add(new Oak(user, (float)Math.random(), (float)Math.random()));
        oakList.add(new Oak(user, (float)Math.random(), (float)Math.random()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_oak_list, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.oakRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
        recyclerView.setAdapter(new OakAdapter(oakList));
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
