package com.krys.exoplayer.ui.videos;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.krys.exoplayer.R;
import com.krys.exoplayer.adapters.AdapterVideoList;
import com.krys.exoplayer.base.BaseFragment;
import com.krys.exoplayer.model.ModelVideo;
import com.krys.exoplayer.player.OfflinePlayer;
import com.krys.exoplayer.utils.CommonUtils;

import java.util.ArrayList;


public class VideosFragment extends BaseFragment {

    private VideosViewModel videosViewModel;
    private AdapterVideoList adapterVideoList;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        videosViewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        init();
        checkPermissions();
    }

    private void findViewById(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_videos);
    }

    private void init() {
        setUpRecycleView();
        setClickListners();
    }

    private void setUpRecycleView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterVideoList = new AdapterVideoList(getActivity());
        recyclerView.setAdapter(adapterVideoList);
    }

    private void checkPermissions() {
        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        loadVideos();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        checkPermissions();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        CommonUtils.showToast(getActivity(), getActivity().getResources().getString(R.string.permission_denied));
                    }
                }).check();
    }

    private void loadVideos() {
        videosViewModel.getVideosList(getActivity()).observe(getViewLifecycleOwner(), new Observer<ArrayList<ModelVideo>>() {
            @Override
            public void onChanged(ArrayList<ModelVideo> modelVideos) {
                adapterVideoList.addAll(modelVideos);
                adapterVideoList.notifyItemInserted(modelVideos.size() - 1);
            }
        });
    }

    private void setClickListners() {
        adapterVideoList.setEventListener(new AdapterVideoList.EventListener() {
            @Override
            public void onVideoClicked(int position) {
                Intent intent = new Intent(getActivity(), OfflinePlayer.class);
                intent.putExtra("videoId", String.valueOf(adapterVideoList.getItemData(position).getId()));
                startActivity(intent);
            }
        });
    }

}