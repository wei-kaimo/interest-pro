<style scoped>
  .operation-button{
    margin-right: 3px;
  }
  .content-layout {
    margin: 40px;
  }
  ul {
    list-style: none;
  }
</style>
<template>
	<div class="content-layout">
        <div>
            <ul>
            	<li>
            		  <Button class="operation-button" type="info" icon="md-add" @click="setBanner()">添加</Button>
                  <Button type="error" icon="md-trash" @click="delBanner()">取消</Button>
                </li>
                <li>
                    <div style="padding: 10px 0;">
                    	<Table border :columns="columns1" :data="data1" :height="520" @on-selection-change="s=>{change(s)}"></Table>
                    </div> 
                </li>
                <li>
                    <div style="text-align: right;">
                        <Page :total="total" :page-size="pageInfo.pageSize" show-elevator show-total @on-change="e=>{pageSearch(e)}"></Page>
                    </div>  
                </li>
            </ul>
        </div>
    </div>
</template>
<script>
export default {
  data() {
    return {
      groupId: [],
      modal: false,
      /*分页total属性绑定值*/
      total: 0,
      /*pageInfo实体*/
      pageInfo: {
        page: 0,
        pageSize: 10
      },
      /*user实体*/
      postcard: {
        id: null,
        username: null,
        title: null,
        interestid: null,
        content: null,
        createtime: null,
        replytime: null
      },
      /*表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "ID",
          key: "id",
          width: 100
        },
        {
          title: "标题",
          key: "title",
          width: 150
        },
        {
          title: "简介",
          key: "info"
        },
        {
          title: "轮播",
          key: "banner",
          width: 100,
          render: (h, params) => {
            if (params.row.banner == 1) {
              return h("div", [
                h(
                  "strong",
                  {
                    style: {
                      color: "#2b85e4"
                    }
                  },
                  "轮播"
                )
              ]);
            } else if (params.row.banner == 0) {
              return h("div", [
                h(
                  "strong",
                  {
                    style: {
                      color: "#f90"
                    }
                  },
                  "非轮播"
                )
              ]);
            }
          }
        },
        {
          title: "操作",
          align: "center",
          key: "action",
          width: 100,
          render: (h, params) => {
            return h(
              "a",
              {
                attrs: {
                  href:
                    this.$store.state.domainName +
                    "/bbs/detail/" +
                    params.row.id,
                  target: "_blank"
                }
              },
              [
                h(
                  "Button",
                  {
                    props: {
                      type: "info"
                    }
                  },
                  "查看"
                )
              ]
            );
          }
        }
      ],
      /*表数据*/
      data1: []
    };
  },
  mounted() {
    /*页面初始化调用方法*/
    this.getTable({
      pageInfo: this.pageInfo
    });
  },
  methods: {
    /*pageInfo实体初始化*/
    initPageInfo() {
      this.pageInfo.page = 0;
      this.pageInfo.pageSize = 10;
    },
    postcardSet(e) {
      this.postcard.id = e.id;
      this.postcard.username = e.username;
      this.postcard.title = e.title;
      this.postcard.interestid = e.interestid;
      this.postcard.content = e.content;
      this.postcard.createtime = e.createtime;
      this.postcard.replytime = e.replytime;
    },
    dateGet(e) {
      var time = new Date(parseInt(e));
      return (
        time.getFullYear() +
        "-" +
        (time.getMonth() + 1) +
        "-" +
        time.getDate() +
        " " +
        time.getHours() +
        ":" +
        time.getMinutes()
      );
    },
    listDateSet(e) {
      for (var i = e.length - 1; i >= 0; i--) {
        e[i].createtime = this.dateGet(e[i].createtime);
      }
    },
    /*得到表数据*/
    getTable(e) {
      this.axios({
        method: "get",
        url: "/interest/bbs/admin/interests",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize
        }
      })
        .then(
          function(response) {
            this.data1 = response.data.data.data;
            // this.listDateSet(this.data1);
            this.total = response.data.data.totalCount;
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo
      });
    },
    /*modal的cancel点击事件*/
    cancel() {
      this.modal = false;
    },
    /*表格中双击事件*/
    dblclick(e) {
      this.postcardSet(e);
      this.modal = true;
    },
    setBanner() {
      if (this.groupId != null && this.groupId != "") {
        this.axios({
          method: "put",
          url: "/interest/bbs/admin/banners/set",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo
              });
              this.groupId = [];
              this.$Message.info("添加成功");
            }.bind(this)
          )
          .catch(function(error) {
            alert(error);
          });
      }else{
        this.$Message.info("请先勾选");
      }
    },
    delBanner() {
      if (this.groupId != null && this.groupId != "") {
        this.axios({
          method: "put",
          url: "/interest/bbs/admin/banners/del",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo
              });
              this.groupId = [];
              this.$Message.info("删除成功");
            }.bind(this)
          )
          .catch(function(error) {
            alert(error);
          });
      }
    },
    change(e) {
      this.setGroupId(e);
    },
    setGroupId(e) {
      this.groupId = [];
      for (var i = 0; i <= e.length - 1; i++) {
        this.groupId.push(e[i].id);
      }
    }
  }
};
</script>
