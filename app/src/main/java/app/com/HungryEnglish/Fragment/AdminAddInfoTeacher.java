package app.com.HungryEnglish.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Presenter.AdminAddInfoTeacherPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.AdminAddInfoTeacherView;
import app.com.HungryEnglish.databinding.DialogAddImageBinding;
import app.com.HungryEnglish.databinding.DialogAddLinkBinding;

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
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_image:

                dialog = new Dialog(getActivity());
                dialogAddImageBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_image, null, false);
                dialog.setContentView(dialogAddImageBinding.getRoot());
                dialogAddImageBinding.submitImg.setOnClickListener(this);
                dialogAddImageBinding.imagePick.setOnClickListener(this);
                dialog.show();

                break;
            case R.id.add_link:
                dialog = new Dialog(getActivity());
                dialogAddLinkBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_link, null, false);
                dialog.setContentView(dialogAddLinkBinding.getRoot());
                dialogAddLinkBinding.submitLink.setOnClickListener(this);
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
                    Toast.makeText(getActivity(), "Work in progress", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_pick:
                Toast.makeText(getActivity(), "Work in progress", Toast.LENGTH_SHORT).show();
                break;
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
