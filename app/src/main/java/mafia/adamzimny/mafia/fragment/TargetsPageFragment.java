package mafia.adamzimny.mafia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.activity.TargetActivity;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.TargetService;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.util.AppVariable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by adamz on 01.08.2016.
 */
public class TargetsPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int currentPage;
    TargetService targetService;
    Call<List<Target>> targetCall;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    public static TargetsPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TargetsPageFragment fragment = new TargetsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = getArguments().getInt(ARG_PAGE);
        targetService = (TargetService) RetrofitBuilder.getService(TargetService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.targets_page_fragment
                , container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        targetCall = targetService.getTargetsForUser(AppVariable.loggedUser.getId(), AppVariable.token);
                        targetCall.enqueue(new Callback<List<Target>>() {
                                               @Override
                                               public void onResponse(Call<List<Target>> call, Response<List<Target>> response) {
                                                   if (response.code() == 200) {
                                                       //TODO populate recyclerview
                                                   }
                                               }

                                               @Override
                                               public void onFailure(Call<List<Target>> call, Throwable t) {
                                                   Toast.makeText(TargetsPageFragment.this.getActivity(),
                                                           "Failed to load. Please try again.",
                                                           Toast.LENGTH_LONG).show();
                                               }
                                           }

                        );
                    }
                }

        );
        return view;
    }
}
