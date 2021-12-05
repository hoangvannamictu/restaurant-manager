package test1.com.quanlyquanlautrungkhanh;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

import test1.com.quanlyquanlautrungkhanh.Adapter.FoodAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.TableAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.Table;

public class FoodActivity extends AppCompatActivity {
    public static final int KEY_ADD_FOOD = 21;
    public static final int KEY_EDIT_FOOD = 22;
    public static final int PERMISSION_RESULT_CODE_ADD_PICTURE = 201;
    public static final int PERMISSION_RESULT_CODE_EDIT_PICTURE = 202;

    private ImageView imgAddPicture_food;
    private ImageView imgEditPicture_food;
    private RecyclerView rcv_food;
    private CardView cvAdd_food;
    private FoodAdapter mFoodAdapter;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;
    private FirebaseStorage mStorage;

    private ActivityResultLauncher<Intent> mActivityResultLauncher_addFood = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() ,uri);
                            imgAddPicture_food.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> mActivityResultLauncher_editFood = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() ,uri);
                            imgEditPicture_food.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance("gs://quan-ly-quan-lau-trung-khanh.appspot.com");

        anhXa();
        readListUserFromFirebase();
        addEvents();

    }

    private void anhXa() {
        rcv_food = findViewById(R.id.rcv_food);
        cvAdd_food = findViewById(R.id.cvAdd_food);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcv_food.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFoodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFoodAdapter.stopListening();
    }

    private void readListUserFromFirebase() {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(ref.child("foods"), Food.class)
                .setLifecycleOwner(this)
                .build();

        mFoodAdapter = new FoodAdapter(options, FoodActivity.this, new FoodAdapter.IClickItemFoodAdapter(){
            @Override
            public void clickDeleteFood(Food model, int position) {
                openDialogDeleteImageFood(position);
            }

            @Override
            public void clickChangeImgFood(Food model, int position) {
                openDialogEditImageFood(model, position);
            }

            @Override
            public void clickEditFood(Food model, int position) {
                openDialogEditFood(model, position);
            }
        });

        rcv_food.setAdapter(mFoodAdapter);
    }

    private void addEvents() {
        cvAdd_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(FoodActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_add_food);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                imgAddPicture_food = dialog.findViewById(R.id.imgAddPicture_food);
                Button btnChoosePicture_food = dialog.findViewById(R.id.btnChoosePicture_food);
                Button btnAdd_food = dialog.findViewById(R.id.btnAdd_food);
                Button btnQuitDiaLogAdd_food = dialog.findViewById(R.id.btnQuitDiaLogAdd_food);
                Button btnAddChooseTag_food = dialog.findViewById(R.id.btnAddChooseTag_food);
                EditText edtAddName_food = dialog.findViewById(R.id.edtAddName_food);
                EditText edtAddAmount_food = dialog.findViewById(R.id.edtAddAmount_food);
                EditText edtAddPrice_food = dialog.findViewById(R.id.edtAddPrice_food);
                TextView tvAddTag_food = dialog.findViewById(R.id.tvAddTag_food);

                imgAddPicture_food.setImageBitmap(conVertImgFoodDefault(R.drawable.ic_food_128));

                btnChoosePicture_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickCheckPermission(KEY_ADD_FOOD);
                    }
                });

                btnAddChooseTag_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenuChooseTagFood(tvAddTag_food , btnAddChooseTag_food);
                    }
                });

                btnAdd_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strAddName_food = edtAddName_food.getText().toString().trim();
                        String strAddAmount_food = edtAddAmount_food.getText().toString().trim();
                        String strAddPrice_food = edtAddPrice_food.getText().toString().trim();
                        String strAddTag_food = tvAddTag_food.getText().toString().trim();
                        String strUrlImg_food = "";
                        int intStatus_food;

                        if (strAddTag_food.equals(getResources().getString(R.string.chon_tag))){
                            Toast.makeText(FoodActivity.this, getResources().getString(R.string.ban_chua_chon_tag), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (strAddAmount_food.isEmpty() && strAddName_food.isEmpty() && strAddPrice_food.isEmpty() && strAddTag_food.isEmpty()){
                            Toast.makeText(FoodActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                        }else {
                            if (Integer.valueOf(strAddAmount_food) > 0 ){
                                intStatus_food = 1;
                            }else {
                                intStatus_food = 0;
                            }
                            Random rd = new Random();
                            String strId_food = getIDFood(strAddName_food)+rd.nextInt(1000);

                            Food mFood = new Food(strId_food, strAddName_food, Integer.valueOf(strAddAmount_food), Integer.valueOf(strAddPrice_food), strUrlImg_food, strAddTag_food, intStatus_food);

                            if(LoginActivity.isInternetConnection(FoodActivity.this)){
                                uploadImgFoodMemory(mFood, KEY_ADD_FOOD);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(FoodActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                btnQuitDiaLogAdd_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void openDialogDeleteImageFood(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(FoodActivity.this, R.style.AlertDialog);
        dialog.setTitle(getResources().getString(R.string.xoa));
        dialog.setMessage(getResources().getString(R.string.ban_cochac_muon_xoakhong));

        dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strImgFood= mFoodAdapter.getItem(position).getImgFood();
                StorageReference photoRef = mStorage.getReferenceFromUrl(strImgFood);

                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteInfoFoodOnFirebaseDatabase(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.khong), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogEditImageFood(Food model, int position) {
        Dialog dialog = new Dialog(FoodActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_img_food);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        imgEditPicture_food = dialog.findViewById(R.id.imgEditPicture_food);
        Button btnEditChoosePicture_food = dialog.findViewById(R.id.btnEditChoosePicture_food);
        Button btnEditPicture_food = dialog.findViewById(R.id.btnEditPicture_food);
        Button btnQuitDiaLogEditPicture_food = dialog.findViewById(R.id.btnQuitDiaLogEditPicture_food);

        Picasso.get().load(model.getImgFood()).into(imgEditPicture_food);

        if (imgEditPicture_food.getDrawable() == null){
            imgEditPicture_food.setImageBitmap(conVertImgFoodDefault(R.drawable.ic_food_128));
        }

        btnEditChoosePicture_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCheckPermission(KEY_EDIT_FOOD);
            }
        });

        btnEditPicture_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImgFoodMemory(model, KEY_EDIT_FOOD);
                dialog.dismiss();
            }
        });

        btnQuitDiaLogEditPicture_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogEditFood(Food model, int position) {
        Dialog dialog = new Dialog(FoodActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_food);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnEdit_food= dialog.findViewById(R.id.btnEdit_food);
        Button btnQuitDiaLogEdit_food = dialog.findViewById(R.id.btnQuitDiaLogEdit_food);
        Button btnEditChooseTag_food = dialog.findViewById(R.id.btnEditChooseTag_food);
        EditText edtEditName_food = dialog.findViewById(R.id.edtEditName_food);
        EditText edtEditAmount_food = dialog.findViewById(R.id.edtEditAmount_food);
        EditText edtEditPrice_food = dialog.findViewById(R.id.edtEditPrice_food);
        TextView tvEditTag_food = dialog.findViewById(R.id.tvEditTag_food);
        RadioButton rbStatusOn_editFood = dialog.findViewById(R.id.rbStatusOn_editFood);
        RadioButton rbStatusOff_editFood = dialog.findViewById(R.id.rbStatusOff_editFood);

        edtEditName_food.setText(String.valueOf(model.getNameFood()));
        edtEditAmount_food.setText(String.valueOf(model.getAmountFood()));
        edtEditPrice_food.setText(String.valueOf(model.getPriceFood()));
        tvEditTag_food.setText(model.getTagFood());


        if (model.getStatusFood() == 1){
            rbStatusOn_editFood.setChecked(true);
        }else {
            rbStatusOff_editFood.setChecked(true);
        }

        btnEditChooseTag_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuChooseTagFood(tvEditTag_food, btnEditChooseTag_food);
            }
        });

        btnEdit_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEditAmount_food = edtEditAmount_food.getText().toString();
                String strEditName_food = edtEditName_food.getText().toString();
                String strEditPrice_food = edtEditPrice_food.getText().toString();
                String strEditTag_food = tvEditTag_food.getText().toString();

                int intEditStatus_food;

                if (rbStatusOff_editFood.isChecked()){
                    intEditStatus_food = 0;
                }else {
                    if (Integer.valueOf(strEditAmount_food) > 0){
                        intEditStatus_food = 1;
                    }else {
                        Toast.makeText(FoodActivity.this, getResources().getString(R.string.soluong_phai_lonhon_0), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(strEditName_food.isEmpty() || strEditAmount_food.isEmpty() || strEditPrice_food.isEmpty() || strEditTag_food.isEmpty()){
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                }else {
                    Food mFood = new Food(model.getIdFood(), strEditName_food, Integer.valueOf(strEditAmount_food), Integer.valueOf(strEditPrice_food), model.getImgFood(), strEditTag_food, intEditStatus_food);
                    mFood.setNameImgFood(model.getNameImgFood());
                    if(LoginActivity.isInternetConnection(FoodActivity.this)){
                        editFoodOnFirebaseDatabase(mFood);
                    }else {
                        Toast.makeText(FoodActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                }

            }
        });

        btnQuitDiaLogEdit_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void deleteInfoFoodOnFirebaseDatabase(int position) {
        String strKeyFood = mFoodAdapter.getItem(position).getIdFood();
        ref.child("foods").child(strKeyFood).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.xoa_thanhcong) , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editFoodOnFirebaseDatabase(Food mFood) {
        ref.child("foods").child(mFood.getIdFood()).setValue(mFood, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.sua_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addFoodOnFirebaseDatabase(Food mFood) {
        ref.child("foods").child(mFood.getIdFood()).setValue(mFood, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.them_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImgFoodMemory(Food mFood, int KEY) {
        String oldUrl = mFood.getImgFood();
        Calendar calendar = Calendar.getInstance();
        String strNameImg_food = mFood.getIdFood()+calendar.getTimeInMillis()+".png";
        StorageReference storageRef = mStorage.getReference();
        StorageReference imgFoodRef = storageRef.child("foods").child(strNameImg_food);
        // Get the data from an ImageView as bytes
        Bitmap bitmap = null;
        if (KEY == KEY_ADD_FOOD){
            imgAddPicture_food.setDrawingCacheEnabled(true);
            imgAddPicture_food.buildDrawingCache();
            bitmap = ((BitmapDrawable) imgAddPicture_food.getDrawable()).getBitmap();
        }else if(KEY == KEY_EDIT_FOOD){
            imgEditPicture_food.setDrawingCacheEnabled(true);
            imgEditPicture_food.buildDrawingCache();
            bitmap = ((BitmapDrawable) imgEditPicture_food.getDrawable()).getBitmap();
        }

        if (bitmap == null){
            Toast.makeText(this, getResources().getString(R.string.loi_anh), Toast.LENGTH_SHORT).show();
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imgFoodRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgFoodRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String strDownloadUri = uri.toString();
                            mFood.setImgFood(strDownloadUri);
                            mFood.setNameImgFood(strNameImg_food);

                            if (KEY == KEY_ADD_FOOD){
                                addFoodOnFirebaseDatabase(mFood);
                            }else if (KEY == KEY_EDIT_FOOD){
                                ref.child("foods").child(mFood.getIdFood()).setValue(mFood, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null){

                                            StorageReference photoRef = mStorage.getReferenceFromUrl(oldUrl);
                                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.sua_thanhcong), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+exception, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else {
                                            Toast.makeText(FoodActivity.this, getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    private void openMenuChooseTagFood(TextView tvTag_food, Button btnChooseTag_food) {
        PopupMenu popupMenu = new PopupMenu(FoodActivity.this , btnChooseTag_food);
        popupMenu.getMenuInflater().inflate(R.menu.menu_tag_food, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuTagFood_doan: {
                        tvTag_food.setText(item.getTitle());
                        break;
                    }

                    case R.id.menuTagFood_douong: {
                        tvTag_food.setText(item.getTitle());
                        break;
                    }

                    case R.id.menuTagFood_hoaqua: {
                        tvTag_food.setText(item.getTitle());
                        break;
                    }

                    default:{
                        tvTag_food.setText(item.getTitle());
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    //convert avt default to bitmap and then set avt default
    private Bitmap conVertImgFoodDefault(int ic_default_food) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_food_128);
        Canvas canvas = new Canvas();
        Bitmap mBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(mBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return mBitmap;
    }

    private void onClickCheckPermission(int KEY_SELECTE_PICTURE){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (KEY_SELECTE_PICTURE == KEY_ADD_FOOD){
                openGallery(PERMISSION_RESULT_CODE_ADD_PICTURE);
            }else if (KEY_SELECTE_PICTURE == KEY_EDIT_FOOD){
                openGallery(PERMISSION_RESULT_CODE_EDIT_PICTURE);
            }
            return;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if (KEY_SELECTE_PICTURE == KEY_ADD_FOOD){
                openGallery(PERMISSION_RESULT_CODE_ADD_PICTURE);
            }else if (KEY_SELECTE_PICTURE == KEY_EDIT_FOOD){
                openGallery(PERMISSION_RESULT_CODE_EDIT_PICTURE);
            }
        }else {
            if (KEY_SELECTE_PICTURE == KEY_ADD_FOOD){
                String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_RESULT_CODE_ADD_PICTURE);
            }else if (KEY_SELECTE_PICTURE == KEY_EDIT_FOOD){
                String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_RESULT_CODE_EDIT_PICTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_RESULT_CODE_ADD_PICTURE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery(PERMISSION_RESULT_CODE_ADD_PICTURE);
            }else {
                Toast.makeText(this, getResources().getString(R.string.ban_chua_cap_quyen_camera), Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == PERMISSION_RESULT_CODE_EDIT_PICTURE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery(PERMISSION_RESULT_CODE_EDIT_PICTURE);
            }else {
                Toast.makeText(this, getResources().getString(R.string.ban_chua_cap_quyen_camera), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery(int PERMISSION_RESULT_CODE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        if (PERMISSION_RESULT_CODE == PERMISSION_RESULT_CODE_ADD_PICTURE){
            mActivityResultLauncher_addFood.launch(Intent.createChooser(intent, "get picture"));
        }else if(PERMISSION_RESULT_CODE == PERMISSION_RESULT_CODE_EDIT_PICTURE){
            mActivityResultLauncher_editFood.launch(Intent.createChooser(intent, "get picture1"));
        }
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

    private String getIDFood(String s) {
        Calendar calendar = Calendar.getInstance();

        s = s.trim().replaceAll("[\\s+\\.\\#\\$\\[\\]\\@]","");
        String output = removeAccent(s);

        return output;
    }
}