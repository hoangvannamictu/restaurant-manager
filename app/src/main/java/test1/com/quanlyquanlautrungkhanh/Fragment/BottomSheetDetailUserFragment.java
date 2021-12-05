package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;
import test1.com.quanlyquanlautrungkhanh.Model.User;
import test1.com.quanlyquanlautrungkhanh.R;

public class BottomSheetDetailUserFragment extends BottomSheetDialogFragment {
    private static final String KEY_GET_USER = "my_user";
    private User mUser;

    private TextView tvDetailName_user, tvDetailEmail_user, tvDetailPassword_user, tvDetailDateCreate_user, tvDetailAge_user, tvDetailAddress_user, tvDetailIdentityCard_user, tvDetailPhone_user, tvDetailStatus_user;
    private CircleImageView imgDetailAvt_user;

    public static BottomSheetDetailUserFragment myInstance(User user){
        BottomSheetDetailUserFragment mBottomSheetDetailUserFragment = new BottomSheetDetailUserFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(KEY_GET_USER, user);

        mBottomSheetDetailUserFragment.setArguments(mBundle);

        return mBottomSheetDetailUserFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            mUser = (User) bundleReceive.get(KEY_GET_USER);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_detail_user, null);
        bottomSheetDialog.setContentView(view);

        anhXa(view);
        setData();
        return bottomSheetDialog;
    }

    private void anhXa(View view){
        imgDetailAvt_user = view.findViewById(R.id.imgDetailAvt_user);
        tvDetailName_user = view.findViewById(R.id.tvDetailName_user);
        tvDetailEmail_user = view.findViewById(R.id.tvDetailEmail_user);
        tvDetailPassword_user = view.findViewById(R.id.tvDetailPassword_user);
        tvDetailAge_user = view.findViewById(R.id.tvDetailAge_user);
        tvDetailDateCreate_user = view.findViewById(R.id.tvDetailDateCreate_user);
        tvDetailAddress_user = view.findViewById(R.id.tvDetailAddress_user);
        tvDetailIdentityCard_user = view.findViewById(R.id.tvDetailIdentityCard_user);
        tvDetailPhone_user = view.findViewById(R.id.tvDetailPhone_user);
        tvDetailStatus_user = view.findViewById(R.id.tvDetailStatus_user);
    }

    private void setData(){
        if (mUser == null){
            return;
        }

        Picasso.get().load(mUser.getImgAvtUser()).error(R.drawable.ic_account_avt_64).into(imgDetailAvt_user);
        tvDetailName_user.setText(mUser.getNameUser());
        tvDetailEmail_user.setText(mUser.getEmailUser());
        tvDetailPassword_user.setText(mUser.getPasswordUser());
        tvDetailAge_user.setText(mUser.getAgeUser());
        tvDetailDateCreate_user.setText(mUser.getDateCreatedUser());
        tvDetailAddress_user.setText(mUser.getAddressUser());
        tvDetailIdentityCard_user.setText(mUser.getIdentityCardUser());
        tvDetailPhone_user.setText(mUser.getPhoneUser());

        if (mUser.getStatusUser() == 1){
            tvDetailStatus_user.setText(getContext().getResources().getString(R.string.trang_thai_hoat_dong));
        }else {
            tvDetailStatus_user.setText(getContext().getResources().getString(R.string.trang_thai_tam_dung));
        }

    }
}
