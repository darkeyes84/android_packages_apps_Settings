/*
 * Copyright (C) 2017 ParanoidAndroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.annotation.Nullable;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.android.settings.widget.SwitchBar;

public class PieSettings extends SettingsPreferenceFragment implements
        SwitchBar.OnSwitchChangeListener, OnPreferenceChangeListener {

    private static final String KEY_PIE_BATTERY = "pie_battery_mode";
    private static final String KEY_PIE_THEME = "pie_theme_mode";
    private static final String KEY_PIE_STATUS = "pie_status_indicator";
    private static final String KEY_PIE_GRAVITY = "pie_gravity";
    private static final String KEY_PIE_GRAVITY_LANDSCAPE = "pie_gravity_landscape";

    private SwitchBar mSwitchBar;
    private ListPreference mTheme;
    private ListPreference mBattery;
    private ListPreference mStatus;
    private ListPreference mPieGravity;
    private ListPreference mPieGravityLandscape;
    private boolean mIsEnabled;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.pie_settings);

        setHasOptionsMenu(true);

        mIsEnabled = Settings.Secure.getIntForUser(getContentResolver(),
                Settings.Secure.PIE_STATE, 0, UserHandle.USER_CURRENT) == 1;

        mTheme = (ListPreference) findPreference(KEY_PIE_THEME);
        if (mTheme != null) {
            int value = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.PIE_THEME_MODE, 0, UserHandle.USER_CURRENT);
            mTheme.setValue(String.valueOf(value));
            mTheme.setOnPreferenceChangeListener(this);
            mTheme.setEnabled(mIsEnabled);
        }

        mBattery = (ListPreference) findPreference(KEY_PIE_BATTERY);
        if (mBattery != null) {
            int value = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.PIE_BATTERY_MODE, 0, UserHandle.USER_CURRENT);
            mBattery.setValue(String.valueOf(value));
            mBattery.setOnPreferenceChangeListener(this);
            mBattery.setEnabled(mIsEnabled);
        }

        mStatus = (ListPreference) findPreference(KEY_PIE_STATUS);
        if (mStatus != null) {
            int value = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.PIE_STATUS_INDICATOR, 0, UserHandle.USER_CURRENT);
            mStatus.setValue(String.valueOf(value));
            mStatus.setOnPreferenceChangeListener(this);
            mStatus.setEnabled(mIsEnabled);
        }

        mPieGravity = (ListPreference) findPreference(KEY_PIE_GRAVITY);
        if (mPieGravity != null) {
            int value = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.PIE_GRAVITY, 0, UserHandle.USER_CURRENT);
            mPieGravity.setValue(String.valueOf(value));
            mPieGravity.setOnPreferenceChangeListener(this);
            mPieGravity.setEnabled(mIsEnabled);
        }

        mPieGravityLandscape = (ListPreference) findPreference(KEY_PIE_GRAVITY_LANDSCAPE);
        if (mPieGravityLandscape != null) {
            int value = Settings.Secure.getIntForUser(getContentResolver(),
                    Settings.Secure.PIE_LEFT_HANDED, 0, UserHandle.USER_CURRENT);
            mPieGravityLandscape.setValue(String.valueOf(value));
            mPieGravityLandscape.setOnPreferenceChangeListener(this);
            mPieGravityLandscape.setEnabled(mIsEnabled);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwitchBar = ((SettingsActivity) getActivity()).getSwitchBar();
        mSwitchBar.addOnSwitchChangeListener(this);
        mSwitchBar.setChecked(mIsEnabled);
        mSwitchBar.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mSwitchBar != null) {
			mSwitchBar.hide();
            mSwitchBar.removeOnSwitchChangeListener(this);
        }
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        mIsEnabled = isChecked;
        Settings.Secure.putIntForUser(getContentResolver(),
                Settings.Secure.PIE_STATE, isChecked ? 1 : 0, UserHandle.USER_CURRENT);
        Settings.Global.putString(getContentResolver(), 
                Settings.Global.POLICY_CONTROL, isChecked ? "immersive.full=*" : "");
        mPieGravity.setEnabled(isChecked);
        mPieGravityLandscape.setEnabled(isChecked);
        mTheme.setEnabled(isChecked);
        mBattery.setEnabled(isChecked);
        mStatus.setEnabled(isChecked);
    }

    @Override
    protected int getMetricsCategory() {
        return -1;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBattery) {
			int value = Integer.parseInt((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PIE_BATTERY_MODE, value, UserHandle.USER_CURRENT);
        } else if (preference == mTheme) {
			int value = Integer.parseInt((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PIE_THEME_MODE, value, UserHandle.USER_CURRENT);
        } else if (preference == mStatus) {
			int value = Integer.parseInt((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PIE_STATUS_INDICATOR, value, UserHandle.USER_CURRENT);
        } else if (preference == mPieGravity) {
			int value = Integer.parseInt((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PIE_GRAVITY, value, UserHandle.USER_CURRENT);
        } else if (preference == mPieGravityLandscape) {
			int value = Integer.parseInt((String) newValue);
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.PIE_LEFT_HANDED, value, UserHandle.USER_CURRENT);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
