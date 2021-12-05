package test1.com.quanlyquanlautrungkhanh.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import test1.com.quanlyquanlautrungkhanh.Model.User;
import test1.com.quanlyquanlautrungkhanh.R;

public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder> {
    private IClickItemUserAdapter iClickItemUserAdapter;

    public UserAdapter(@NonNull FirebaseRecyclerOptions<User> options, IClickItemUserAdapter iClickItemUserAdapter) {
        super(options);
        this.iClickItemUserAdapter = iClickItemUserAdapter;
    }

    public interface IClickItemUserAdapter{
        void clickDeleteAccountUser(User model, int position);

        void clicDetailAccountUser(User model, int position);

        void clickEditGeneralInfoUser(User model, int position);

        void clickEditAvtUser(User model, int position);
    }


    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position, @NonNull User model) {
        holder.tvEmail_user.setText(model.getEmailUser());
        holder.tvName_user.setText(model.getNameUser());
        holder.tvDateCreate_user.setText(model.getDateCreatedUser());

        Picasso.get().load(model.getImgAvtUser()).error(R.drawable.ic_account_avt_64).into(holder.imgAvt_user);

        if (model.getStatusUser() == 1){
            holder.imgStatus_user.setImageResource(R.drawable.ic_status_on_24);
        }else {
            holder.imgStatus_user.setImageResource(R.drawable.ic_status_off_24);
        }

        holder.layoutDelete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUserAdapter.clickDeleteAccountUser(model, position);
            }
        });

        holder.layoutDetail_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUserAdapter.clicDetailAccountUser(model, position);
            }
        });

        holder.layoutEditGeneralInfo_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUserAdapter.clickEditGeneralInfoUser(model, position);
            }
        });

        holder.layoutEditAvt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUserAdapter.clickEditAvtUser(model, position);
            }
        });
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutDetail_user, layoutEditGeneralInfo_user, layoutDelete_user, layoutEditAvt_user;
        private CircleImageView imgAvt_user;
        private TextView tvEmail_user, tvName_user, tvDateCreate_user;
        private ImageView imgStatus_user;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStatus_user = itemView.findViewById(R.id.imgStatus_user);
            imgAvt_user = itemView.findViewById(R.id.imgAvt_user);
            tvEmail_user = itemView.findViewById(R.id.tvEmail_user);
            tvEmail_user = itemView.findViewById(R.id.tvEmail_user);
            tvName_user = itemView.findViewById(R.id.tvName_user);
            tvDateCreate_user = itemView.findViewById(R.id.tvDateCreate_user);
            layoutDetail_user = itemView.findViewById(R.id.layoutDetail_user);
            layoutEditGeneralInfo_user = itemView.findViewById(R.id.layoutEditGeneralInfo_user);
            layoutDelete_user = itemView.findViewById(R.id.layoutDelete_user);
            layoutEditAvt_user = itemView.findViewById(R.id.layoutEditAvt_user);

        }
    }
}

