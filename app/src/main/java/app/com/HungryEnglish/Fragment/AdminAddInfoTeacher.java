package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.com.HungryEnglish.Adapter.TeacherImageAdapter;
import app.com.HungryEnglish.Adapter.TitleLinkAdapter;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Presenter.AdminAddInfoTeacherPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.AdminAddInfoTeacherView;
import app.com.HungryEnglish.databinding.DialogAddImageBinding;
import app.com.HungryEnglish.databinding.DialogAddLinkBinding;
import app.com.HungryEnglish.databinding.FragmentAdminAddInfoTeacherBinding;

import static app.com.HungryEnglish.Util.RestConstant.PICK_IMAGE;
import static app.com.HungryEnglish.Util.Utils.getBitmapFromUri;
import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AdminAddInfoTeacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddInfoTeacher extends Fragment implements View.OnClickListener, AdminAddInfoTeacherView {
    public static final String IS_ADMIN = "isAdmin";
    private Dialog dialog;
    private DialogAddLinkBinding dialogAddLinkBinding;
    private DialogAddImageBinding dialogAddImageBinding;
    private AdminAddInfoTeacherPresenter presenter;
    private File pickedFile;
    private FragmentAdminAddInfoTeacherBinding binding;
    private boolean isAdmin;
    private Handler handler;
    private int countOfImages;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int curIndex = binding.viewPager.getCurrentItem() + 1;
            if (curIndex == countOfImages) {
                binding.viewPager.setCurrentItem(0);
            } else {
                binding.viewPager.setCurrentItem(curIndex);
            }
            handler.postDelayed(this, 4000);
        }
    };

    public AdminAddInfoTeacher() {
    }

    // TODO: Rename and change types and number of parameters
    public static AdminAddInfoTeacher newInstance() {
        AdminAddInfoTeacher fragment = new AdminAddInfoTeacher();
        return fragment;
    }

    public static AdminAddInfoTeacher newInstance(Boolean isteacher) {
        AdminAddInfoTeacher fragment = new AdminAddInfoTeacher();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_ADMIN, isteacher);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isAdmin = getArguments().getBoolean(IS_ADMIN);
        } else {
            isAdmin = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getTeacherData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new AdminAddInfoTeacherPresenter(getActivity());
        if (isAdmin) {
            setHasOptionsMenu(true);
        }
        binding = FragmentAdminAddInfoTeacherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_teacher, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_image:
                dialog = new Dialog(getActivity());
                dialogAddImageBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_image, null, false);
                dialog.setContentView(dialogAddImageBinding.getRoot());
                dialogAddImageBinding.submitImg.setOnClickListener(this);
                dialogAddImageBinding.imagePick.setOnClickListener(this);
                dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;
            case R.id.add_link:
                dialog = new Dialog(getActivity());
                dialogAddLinkBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_link, null, false);
                dialog.setContentView(dialogAddLinkBinding.getRoot());
                dialogAddLinkBinding.submitLink.setOnClickListener(this);
                dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_link:
                if (dialog != null) {
                    presenter.addLink(String.valueOf(dialogAddLinkBinding.edtTitle.getText()), String.valueOf(dialogAddLinkBinding.edtLink.getText()));
                }
                break;
            case R.id.submit_img:
                if (dialog != null) {
                    String link = dialogAddImageBinding.edtLink.getText().toString();
                    presenter.addImage(pickedFile, link);
                }
                break;
            case R.id.image_pick:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        String path = "";
                        if (Build.VERSION.SDK_INT <= 21) {
                            path = getRealPathFromURI(getActivity(), uri);
                        } else {
                            path = getPath(getActivity(), data.getData());
                        }
                        pickedFile = new File(path);
                        try {
                            dialogAddImageBinding.imagePick.setImageBitmap(getBitmapFromUri(getActivity(), uri));
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Image upload is not working.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void showProgressDialog() {
        Utils.showDialog(getActivity());
    }

    @Override
    public void hideProgressDialog() {
        Utils.dismissDialog();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getErrorInAddText(String string) {
        if (dialog != null) {
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addData(AdminAddInfoResponse basicResponse) {
        if (dialog != null) {
            dialog.dismiss();
            Toast.makeText(getActivity(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (presenter != null) {
                presenter.getTeacherData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void showSliderData(ArrayList<AdminAddInfoDetail> sliderArray) {
        binding.viewPager.setAdapter(new TeacherImageAdapter(getActivity(), sliderArray));
        binding.tablayout.setupWithViewPager(binding.viewPager);
        countOfImages = sliderArray.size();
        handler = new Handler();
        handler.postDelayed(runnable, 4000);
    }

    @Override
    public void showLinkData(ArrayList<AdminAddInfoDetail> linkArray) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(new TitleLinkAdapter(getActivity(), linkArray));
    }
}
