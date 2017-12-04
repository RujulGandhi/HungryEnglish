package app.com.HungryEnglish.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Presenter.AdminAddInfoTeacherPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.AdminAddInfoTeacherView;
import app.com.HungryEnglish.databinding.DialogAddImageBinding;
import app.com.HungryEnglish.databinding.DialogAddLinkBinding;

import static app.com.HungryEnglish.Util.Constant.PICK_IMAGE;
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

    private Dialog dialog;
    private DialogAddLinkBinding dialogAddLinkBinding;
    private DialogAddImageBinding dialogAddImageBinding;
    private AdminAddInfoTeacherPresenter presenter;
    private File pickedFile;

    public AdminAddInfoTeacher() {
    }

    // TODO: Rename and change types and number of parameters
    public static AdminAddInfoTeacher newInstance() {
        AdminAddInfoTeacher fragment = new AdminAddInfoTeacher();
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
        presenter = new AdminAddInfoTeacherPresenter(getActivity());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_admin_add_info_teacher, container, false);
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
}