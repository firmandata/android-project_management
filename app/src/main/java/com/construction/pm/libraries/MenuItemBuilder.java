package com.construction.pm.libraries;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.construction.pm.utils.ViewUtil;

public class MenuItemBuilder {

    protected Context mContext;

    protected MenuItem mMenuItem;

    protected AppCompatTextView mTvText;
    protected AppCompatImageView mIvIcon;

    protected CharSequence mLabel;
    protected Drawable mIcon;

    protected MenuItemWithLabelListener mMenuItemWithLabelListener;

    public MenuItemBuilder(Context context, MenuItem menuItem) {
        mContext = context;
        mMenuItem = menuItem;
    }

    public void buildTopLabel(Drawable icon, CharSequence label) {
        if (Build.VERSION.SDK_INT > 10) {
            mMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            final LinearLayout containerButton = new LinearLayout(mContext);
            containerButton.setOrientation(LinearLayout.VERTICAL);
            containerButton.setGravity(Gravity.CENTER);

            mTvText = new AppCompatTextView(mContext);
            mTvText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvText.setTextSize(8);
            mTvText.setTextColor(Color.WHITE);
            containerButton.addView(mTvText);

            mIvIcon = new AppCompatImageView(mContext);
            mIvIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mIvIcon.setPadding(ViewUtil.getPxFromDp(mContext, 16), 0, ViewUtil.getPxFromDp(mContext, 16), 0);
            containerButton.addView(mIvIcon);

            containerButton.setClickable(true);
            containerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMenuItemWithLabelListener != null)
                        mMenuItemWithLabelListener.onMenuItemClick();
                }
            });

            mMenuItem.setActionView(containerButton);

            setIcon(icon);
            setLabel(label);
        } else {
            buildNormal(icon);
        }
    }

    public void buildRightLabel(Drawable icon, CharSequence label) {
        if (Build.VERSION.SDK_INT > 10) {
            mMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

            final LinearLayout containerButton = new LinearLayout(mContext);
            containerButton.setOrientation(LinearLayout.HORIZONTAL);
            containerButton.setGravity(Gravity.CENTER);

            mIvIcon = new AppCompatImageView(mContext);
            mIvIcon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mIvIcon.setPadding(ViewUtil.getPxFromDp(mContext, 16), 0, 0, 0);
            containerButton.addView(mIvIcon);

            mTvText = new AppCompatTextView(mContext);
            mTvText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvText.setTextSize(18);
            mTvText.setPadding(0, 0, ViewUtil.getPxFromDp(mContext, 16), 0);
            mTvText.setTextColor(Color.WHITE);
            containerButton.addView(mTvText);

            containerButton.setClickable(true);
            containerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMenuItemWithLabelListener != null)
                        mMenuItemWithLabelListener.onMenuItemClick();
                }
            });

            mMenuItem.setActionView(containerButton);

            setIcon(icon);
            setLabel(label);
        } else {
            buildNormal(icon);
        }
    }

    public void buildNormal(Drawable icon) {
        mMenuItem.setIcon(icon);

        if (Build.VERSION.SDK_INT > 10) {
            mMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mMenuItemWithLabelListener != null) {
                    mMenuItemWithLabelListener.onMenuItemClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void buildNormalPriorityNo(Drawable icon) {
        mMenuItem.setIcon(icon);

        if (Build.VERSION.SDK_INT > 10) {
            mMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mMenuItemWithLabelListener != null) {
                    mMenuItemWithLabelListener.onMenuItemClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void buildNormalPriority(Drawable icon) {
        mMenuItem.setIcon(icon);

        if (Build.VERSION.SDK_INT > 10) {
            mMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }


        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mMenuItemWithLabelListener != null) {
                    mMenuItemWithLabelListener.onMenuItemClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        if (mIvIcon != null && Build.VERSION.SDK_INT > 10)
            mIvIcon.setImageDrawable(mIcon);
        else
            mMenuItem.setIcon(mIcon);
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setLabel(CharSequence label) {
        mLabel = label;
        if (mTvText != null && Build.VERSION.SDK_INT > 10)
            mTvText.setText(mLabel);
        else
            mMenuItem.setTitle(mLabel);
    }

    public CharSequence getLabel() {
        return mLabel;
    }

    public void setMenuItemWithLabelListener(MenuItemWithLabelListener menuItemWithLabelListener) {
        mMenuItemWithLabelListener = menuItemWithLabelListener;
    }

    public MenuItemWithLabelListener getMenuItemWithLabelListener() {
        return mMenuItemWithLabelListener;
    }

    public interface MenuItemWithLabelListener {
        void onMenuItemClick();
    }
}
