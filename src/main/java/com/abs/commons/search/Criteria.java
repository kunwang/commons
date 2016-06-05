package com.abs.commons.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hao.wang
 * @since 2016/4/27 22:52
 */
public class Criteria {

    private static final String IS = "is";
    private static final String IS_NOT = "is not";
    private static final String EQ = "=";
    private static final String NE = "!=";
    private static final String LT = "<";
    private static final String LTE = "<=";
    private static final String GT = ">";
    private static final String GTE = ">=";
    private static final String IN = "in";
    private static final String NOT_IN = "not in";
    private static final String NOT_EQ = "!=";
    private static final String LIKE = "like";
    private static final String ILIKE = "ilike";
    private static final String INCLUDE = "INCLUDE";
    private static final String NOT_INCLUDE = "NOT_INCLUDE";

    private List<Criteria> criteriaChain;
    private String key;
    private String operator;
    private Object value;

    public Criteria() {
        this.criteriaChain = new ArrayList<Criteria>();
    }

    public Criteria(String key) {
        this.criteriaChain = new ArrayList<Criteria>();
        this.criteriaChain.add(this);
        this.key = key;
    }

    protected Criteria(List<Criteria> criteriaChain, String key) {
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
        this.key = key;
    }

    public static Criteria where(String key) {
        return new Criteria(key);
    }

    public Criteria and(String key) {
        return new Criteria(this.criteriaChain, key);
    }

    public Criteria is(Object o) {
        this.value = o;
        String operator;
        if (o == null) {
            operator = IS;
        } else {
            operator = EQ;
        }
        return this.set(operator, o);
    }

    public Criteria ne(Object o) {
        assertNull(o);
        return this.set(NE, o);
    }

    public Criteria lt(Object o) {
        assertNull(o);
        return this.set(LT, o);
    }

    public Criteria lte(Object o) {
        assertNull(o);
        return this.set(LTE, o);
    }

    public Criteria gt(Object o) {
        assertNull(o);
        return this.set(GT, o);
    }

    public Criteria gte(Object o) {
        assertNull(o);
        return this.set(GTE, o);
    }

    public Criteria like(Object o) {
        assertNull(o);
        return this.set(LIKE, "%" + o + "%");
    }

    public Criteria ilike(Object o) {
        assertNull(o);
        return this.set(ILIKE, "%" + o + "%");
    }

    public Criteria in(Collection<?> c) {
        assertNull(c);
        return this.set(IN, c);
    }

    public Criteria nin(Collection<?> c) {
        assertNull(c);
        return this.set(NOT_IN, c);
    }

    public Criteria include(long bit) {
        return this.set(INCLUDE, bit);
    }

    public Criteria notInclude(long bit) {
        return this.set(NOT_INCLUDE, bit);
    }

    private Criteria not(Object o) {
        this.value = o;
        String operator;
        if (o == null) {
            operator = IS_NOT;
        } else {
            operator = NOT_EQ;
        }
        return this.set(operator, o);
    }

    private Criteria set(String operator, Object value) {
        this.operator = operator;
        this.value = value;
        return this;
    }

    public String getKey() {
        return key;
    }

    private static final Joiner AND_JOINER = Joiner.on(" and ");

    public String getSqlCriteria() {
        List<String> criterias = Lists.newArrayListWithExpectedSize(this.criteriaChain.size());
        StringBuilder sb;
        for (Criteria criteria : this.criteriaChain) {
            sb = new StringBuilder();
            switch (criteria.operator) {
            case IN:
            case NOT_IN:
                sb.append(criteria.key).append(" ").append(criteria.operator).append(" (:").append(criteria.key)
                        .append(")");
                break;
            case IS:
            case IS_NOT:
                sb.append(criteria.key).append(" ").append(criteria.operator).append(" NULL");
                break;
            case INCLUDE:
                sb.append("(").append(criteria.key).append(" & :").append(criteria.key).append(") = :").append(criteria.key);
                break;
            case NOT_INCLUDE:
                sb.append("(").append(criteria.key).append(" & :").append(criteria.key).append(") != :").append(criteria.key);
                break;
            default:
                sb.append(criteria.key).append(" ").append(criteria.operator).append(" :").append(criteria.key);
                break;
            }
            criterias.add(sb.toString());
        }
        return "( " + AND_JOINER.join(criterias) + " )";
    }

    public Map<String, Object> getSqlParameter() {
        Map<String, Object> parameters = Maps.newHashMap();
        for (Criteria criteria : this.criteriaChain) {
            if (criteria.value != null) {
                parameters.put(criteria.key, criteria.value);
            }
        }
        return parameters;
    }

    private void assertNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

}
