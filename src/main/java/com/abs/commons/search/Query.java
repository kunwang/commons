package com.abs.commons.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hao.wang
 * @since 2016/4/27 22:51
 */
public class Query {

    private List<Criteria> criterias = Lists.newArrayList();
    private String table;
    private String returnColumns;
    private Sort sort;
    private int skip;
    private int limit;

    public Query(String table) {
        this.table = table;
    }

    public Query criteria(Criteria criteria) {
        this.criterias.add(criteria);
        return this;
    }

    public Query skip(int skip) {
        this.skip = skip;
        return this;
    }

    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Query with(Sort sort) {
        if (sort == null) {
            return this;
        }
        if (this.sort == null) {
            this.sort = sort;
        } else {
            this.sort = this.sort.and(sort);
        }
        return this;
    }

    public Query returnColumns(String returnColumns) {
        this.returnColumns = returnColumns;
        return this;
    }

    protected String getReturnColumns() {
        return StringUtils.defaultIfEmpty(returnColumns, "*");
    }

    private static final Joiner AND_JOINER = Joiner.on(" and ");

    public String getSql() {
        return getSql(false);
    }

    public String getSql(boolean isPG) {
        StringBuilder sb = new StringBuilder("select ").append(getReturnColumns()).append(" from ").append(this.table);

        sb.append(this.getCriteriaSql());
        if (this.sort != null) {
            sb.append(" ").append(this.sort.getSqlSort());
        }
        if (this.limit != 0) {
            if (isPG) {
                sb.append(" offset :skip limit :limit");
            } else {
                sb.append(" limit :skip,:limit");
            }
        }
        sb.append(" ;");
        return sb.toString();
    }

    public String getCountSql() {
        StringBuilder sb = new StringBuilder("select count(1) from ").append(this.table);
        sb.append(this.getCriteriaSql());
        sb.append(" ;");
        return sb.toString();
    }

    public Map<String,Object> getCountSqlParameter() {
        Map<String,Object> parameters = Maps.newHashMap();
        if (!isEmpty(this.criterias)) {
            for (Criteria criteria : this.criterias) {
                parameters.putAll(criteria.getSqlParameter());
            }
        }
        return parameters;
    }

    private String getCriteriaSql() {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(this.criterias)) {
            sb.append(" where ");
            List<String> criterias = Lists.newArrayListWithExpectedSize(this.criterias.size());
            criterias.addAll(this.criterias.stream().map(Criteria::getSqlCriteria).collect(Collectors.toList()));
            sb.append(AND_JOINER.join(criterias));
        }
        return sb.toString();
    }

    public Map<String,Object> getSqlParameter() {
        Map<String,Object> source = Maps.newHashMap();
        if (!isEmpty(this.criterias)) {
            for (Criteria criteria : this.criterias) {
                source.putAll(criteria.getSqlParameter());
            }
        }
        if (this.limit != 0) {
            source.put("skip", this.skip);
            source.put("limit", this.limit);
        }
        return source;
    }

    private boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }
}
