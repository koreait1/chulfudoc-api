package org.backend.chulfudoc.board.controllers;

import lombok.Data;
import org.backend.chulfudoc.global.search.CommonSearch;

import java.util.List;

@Data
public class BoardSearch extends CommonSearch {

    private List<String> bid;
    private List<String> category;
    private List<String> userId;

}
