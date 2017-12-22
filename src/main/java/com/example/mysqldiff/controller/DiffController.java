package com.example.mysqldiff.controller;

import com.example.mysqldiff.service.DiffService;
import com.example.mysqldiff.vo.ColumnDiffVo;
import com.example.mysqldiff.vo.SyncDirectionEnum;
import com.example.mysqldiff.vo.TableDiffVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "diff")
public class DiffController {

    @Resource
    private DiffService diffService;

    @RequestMapping(path = "table", method = RequestMethod.GET)
    public List<ColumnDiffVo> diffTable(
            @RequestParam("table1") String table1,
            @RequestParam(value = "table2", required = false) String table2
    ) {
        if (StringUtils.isEmpty(table2)) {
            table2 = table1;
        }
        List<ColumnDiffVo> diffVos = diffService.compareTable(table1, table2);
        return diffVos;
    }

    @RequestMapping(path = "db", method = RequestMethod.GET)
    public List<TableDiffVo> diffDb() {
        List<TableDiffVo> diffVos = diffService.compareDb();
        return diffVos;
    }

    @RequestMapping(path = "sync-db", method = RequestMethod.GET)
    public Map<String, List<String>> syncDb(@RequestParam("syncDirection")SyncDirectionEnum syncDirection) {
        return diffService.syncDb(syncDirection);
    }
}
