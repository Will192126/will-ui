package com.will.ui.refresh;

public interface IWiRefresh {
    void setDisableRefreshScroll(boolean isDisabled);

    void refreshFinished();

    void setRefreshOverView(WiOverView overView);

    void setRefreshListener(RefreshListener listener);

    interface RefreshListener {
        void onRefresh();

        boolean enableRefresh();
    }
}
