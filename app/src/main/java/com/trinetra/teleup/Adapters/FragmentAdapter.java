package com.trinetra.teleup.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.trinetra.teleup.Fragments.Chat;
import com.trinetra.teleup.Fragments.Fragment_status;
import com.trinetra.teleup.Fragments.Users;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new Chat();
            case 1: return new Users();
            case 2: return new Fragment_status();

            default: return new Chat();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if (position==0){
            title="Chat";
        }
        else if (position==1){
            title="Users";
        }
        else if (position==2){
            title = "Status";
        }

        return title;
    }
}
