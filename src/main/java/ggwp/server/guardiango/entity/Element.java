package ggwp.server.guardiango.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Element {
    List<CCTV> cctvs = new ArrayList<CCTV>();
}
