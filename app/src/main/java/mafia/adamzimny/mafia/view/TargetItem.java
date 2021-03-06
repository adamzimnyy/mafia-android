package mafia.adamzimny.mafia.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.util.helper.ImgurHelper;
import mafia.adamzimny.mafia.constant.Targets;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.util.DateUtils;
import mafia.adamzimny.mafia.util.helper.LocationHelper;

/**
 * Created by adamz on 18.08.2016.
 */
public class TargetItem extends AbstractItem<TargetItem, TargetItem.ViewHolder> {
    Target target;
    Context context;
    //The unique ID for this type of item
    CountDownTimer timer;

    public TargetItem withTarget(Target target) {
        this.target = target;
        return this;
    }

    public TargetItem withContext(Context context) {
        this.context = context;
        return this;
    }

    @Override
    public int getType() {
        return R.id.target_list_item;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.list_element;
    }

    public Target getTarget() {
        return target;
    }

    public void stopTimer() {
        Log.d("timer","stopTimer");
        if (timer != null)
            timer.cancel();
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(final ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        viewHolder.name.setText(target.getHunted().getFirstName() + " " +
                target.getHunted().getLastName() + ", " +
                DateUtils.getAgeFromBirthDate(target.getHunted().getDateOfBirth()));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(ImgurHelper.compileUrl(target.getHunted().getProfilePicture()), viewHolder.profileImage);
        viewHolder.distance.setText(String.valueOf(LocationHelper.distance(target)));
        switch (target.getStatus()) {
            case Targets.ACTIVE:
                timer = new CountDownTimer(DateUtils.timeLeftOnTargetFromCreatedDateAsSeconds(target.getCreated()), 1000) {

                    public void onTick(long millisUntilFinished) {
                        viewHolder.timeLeft.setText(DateUtils.timeLeftOnTargetFromCreatedDate(target.getCreated()));
                     }

                    @Override
                    public void onFinish() {
                    }
                };
                timer.start();
                viewHolder.statusBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.target_yellow));
                break;
            case Targets.FAILED:
                Log.d("target", "viewholder status failed");
                viewHolder.timeLeft.setText(Targets.FAILED);
                viewHolder.statusBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.target_red));
                if (timer != null) timer.cancel();
                break;
            case Targets.COMPLETED:
                Log.d("target", "viewholder status completed");
                viewHolder.timeLeft.setText(Targets.COMPLETED);
                viewHolder.statusBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.target_green));
                if (timer != null) timer.cancel();
                break;
        }
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        protected TextView name;

        @BindView(R.id.profile_image)
        ImageView profileImage;

        @BindView(R.id.text_distance)
        TextView distance;

        @BindView(R.id.text_time_left)
        TextView timeLeft;

        @BindView(R.id.status_view)
        View statusBarView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

