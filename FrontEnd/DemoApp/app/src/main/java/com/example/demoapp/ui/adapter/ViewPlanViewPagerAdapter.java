package com.example.demoapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.ui.main.plan.memory.AddMemoryFragment;
import com.example.demoapp.ui.main.plan.moment.AddMomentFragment;
import com.example.demoapp.ui.main.plan.view.activities.ActivityFragment;
import com.example.demoapp.ui.main.plan.view.comments.CommentFragment;

public class ViewPlanViewPagerAdapter extends FragmentStateAdapter
{
    public ViewPlanViewPagerAdapter(Fragment fragment)
    {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                return new ActivityFragment();
            case 1:
                return new CommentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
