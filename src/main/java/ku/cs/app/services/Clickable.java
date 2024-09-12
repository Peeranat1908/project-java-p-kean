package ku.cs.app.services;

import ku.cs.app.models.Report;

import java.io.IOException;
public interface Clickable {
    public void onClickList(Report report);

    public void onClickLike(Report report) throws IOException;
    //public void onClickInformAbuse(Report report) throws IOException;
}
