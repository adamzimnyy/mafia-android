package mafia.adamzimny.mafia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.activity.DetailsActivity;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.TargetService;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.util.helper.IntentHelper;
import mafia.adamzimny.mafia.view.TargetItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TargetsPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String IS_PUBLIC = "IS_PUBLIC";

    private int currentPage;
    private boolean isPublic;
    TargetService targetService;
    Call<List<Target>> targetCall;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    List<TargetItem> recyclerItems;
    FastItemAdapter fastAdapter;

    public static TargetsPageFragment newInstance(int page, boolean isPublic) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putBoolean(IS_PUBLIC, isPublic);
        TargetsPageFragment fragment = new TargetsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = getArguments().getInt(ARG_PAGE);
        isPublic = getArguments().getBoolean(IS_PUBLIC);
        targetService = (TargetService) RetrofitBuilder.getService(TargetService.class, RetrofitBuilder.BASE_URL);
        recyclerItems = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_targets_recycler
                , container, false);
        ButterKnife.bind(this, view);
        fastAdapter = new FastItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<TargetItem>() {
            @Override
            public boolean onClick(View v, IAdapter<TargetItem> adapter, TargetItem item, int position) {
               openDetails(item.getTarget());
                return true;
            }
        });
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                      refresh();
                    }
                }

        );
        refresh();
        return view;
    }

    private void refresh() {
        for (TargetItem t : recyclerItems) {
            t.stopTimer();
        }
        recyclerItems.clear();
        fastAdapter.clear();
        targetCall = targetService.getTargetsForUser(AppVariable.loggedUser.getId(), AppVariable.token);
        targetCall.enqueue(new Callback<List<Target>>() {
                               @Override
                               public void onResponse(Call<List<Target>> call, Response<List<Target>> response) {
                                   if (response.code() == 200) {
                                       List<Target> targets = response.body();
                                       Collections.sort(targets);
                                       List<TargetItem> items = new ArrayList<>();
                                       for (Target t : targets) {
                                           if(t.isPublicTarget() == isPublic)
                                               items.add(new TargetItem().withTarget(t).withContext(getActivity()));
                                       }
                                       refreshLayout.setRefreshing(false);
                                       recyclerItems.addAll(items);
                                       fastAdapter.add(items);
                                       fastAdapter.notifyDataSetChanged();
                                   }
                               }

                               @Override
                               public void onFailure(Call<List<Target>> call, Throwable t) {
                                   Log.d("refreshTarget", "failure");
                                   Toast.makeText(TargetsPageFragment.this.getActivity(),
                                           "Failed to load. Please try again.",
                                           Toast.LENGTH_LONG).show();
                               }
                           }

        );
    }

    private void openDetails(Target target) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("target",target);
        IntentHelper.startActivityIntent(getActivity(), DetailsActivity.class,bundle);
    }
}
