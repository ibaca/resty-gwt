package org.fusesource.restygwt.client;

public final class OverlayMethodCallback<T> implements OverlayCallback<T> {
    private final MethodCallback<T> adaptee;

    public OverlayMethodCallback(MethodCallback<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override public void onFailure(Method method, Throwable exception) {
        adaptee.onFailure(method, exception);
    }

    @Override public void onSuccess(Method method, T response) {
        adaptee.onSuccess(method, response);
    }
}
