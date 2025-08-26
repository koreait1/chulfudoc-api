package org.backend.chulfudoc.member.controllers;

import lombok.Data;
import org.backend.chulfudoc.global.search.CommonSearch;
import org.backend.chulfudoc.member.constants.Authority;

import java.util.List;

@Data
public class MemberSearch extends CommonSearch {
    private List<Authority> authorities;
}
