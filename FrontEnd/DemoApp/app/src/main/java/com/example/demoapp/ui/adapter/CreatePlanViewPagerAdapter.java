package com.example.demoapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.demoapp.ui.main.plan.memory.AddMemoryFragment;
import com.example.demoapp.ui.main.plan.moment.AddMomentFragment;

public class CreatePlanViewPagerAdapter extends FragmentStateAdapter
{
    public CreatePlanViewPagerAdapter(Fragment fragment)
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
                return new AddMemoryFragment();
            case 1:
                return new AddMomentFragment();
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
