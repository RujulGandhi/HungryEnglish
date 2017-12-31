package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import app.com.HungryEnglish.Interface.AdminAddInfoStudentView;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Presenter.AdminAddInfoStudentPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.DialogAddImageBinding;
import app.com.HungryEnglish.databinding.FragmentAdminAddInfoStudentBinding;

import static app.com.HungryEnglish.Util.RestConstant.PICK_IMAGE;
import static app.com.HungryEnglish.Util.Utils.getBitmapFromUri;
import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AdminAddInfoStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddInfoStudent extends Fragment implements View.OnClickListener, AdminAddInfoStudentView {

    private DialogAddImageBinding dialogBinding;
    private Dialog dialog;
    private AdminAddInfoStudentPresenter presenter;
    private File pickedFile;
    private FragmentAdminAddInfoStudentBinding binding;
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

    public AdminAddInfoStudent() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdminAddInfoStudent newInstance() {
        AdminAddInfoStudent fragment = new AdminAddInfoStudent();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new AdminAddInfoStudentPresenter(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        handler = new Handler();
        binding = FragmentAdminAddInfoStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_student, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_link:
                dialog = new Dialog(getActivity());
                dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_image, null, false);
                dialog.setContentView(dialogBinding.getRoot());
                dialogBinding.submitImg.setOnClickListener(this);
                dialogBinding.imagePick.setOnClickListener(this);
                dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_img:
                if (dialogBinding != null && dialog != null) {
                    String link = dialogBinding.edtLink.getText().toString();
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
                            dialogBinding.imagePick.setImageBitmap(getBitmapFromUri(getActivity(), uri));
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Image upload is not working.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
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
    public void showProgressDialog() {
        Utils.showDialog(getActivity());
    }

    @Override
    public void hideProgressDialog() {
        Utils.dismissDialog();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getErrorInAddText(String message) {
        if (dialog != null) {
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSliderData(ArrayList<AdminAddInfoDetail> data) {
        binding.viewPager.setAdapter(new TeacherImageAdapter(getActivity(), data));
        binding.tablayout.setupWithViewPager(binding.viewPager);
        countOfImages = data.size();
        handler = new Handler();
        handler.postDelayed(runnable, 4000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            presenter.getStudentData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

}
