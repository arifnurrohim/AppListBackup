package net.zemna.android.market;

import android.content.Context;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes.dex */
public final class MarketProviderFactory {
    public static Hashtable<String, IMarketProvider> getAvailableMarketProviders(Context context) {
        Hashtable<String, IMarketProvider> providers = new Hashtable<>();
        IMarketProvider p = new AndroidMarketProvider(context);
        if (p.isAvailable()) {
            providers.put(p.getKey(), p);
        }
        IMarketProvider p2 = new TStoreMarketProvider(context);
        if (p2.isAvailable()) {
            providers.put(p2.getKey(), p2);
        }
        return providers;
    }
}
