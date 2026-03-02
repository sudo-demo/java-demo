<template>
  <el-popover
    placement="bottom-start"
    width="400"
    trigger="click"
    v-model="visible"
  >
    <div class="icon-selector">
      <!-- 搜索框 -->
      <el-input
        v-model="searchText"
        placeholder="搜索图标"
        prefix-icon="el-icon-search"
        size="small"
        clearable
        class="search-input"
      />
      
      <!-- 图标列表 -->
      <div class="icon-list">
        <div
          v-for="icon in filteredIcons"
          :key="icon"
          :class="['icon-item', { 'is-active': selectedIcon === icon }]"
          @click="selectIcon(icon)"
          :title="icon"
        >
          <i :class="icon"></i>
        </div>
        <div v-if="filteredIcons.length === 0" class="empty-text">
          未找到匹配的图标
        </div>
      </div>
    </div>
    
    <!-- 触发按钮 -->
    <el-input
      slot="reference"
      v-model="displayValue"
      :placeholder="placeholder"
      readonly
      size="small"
    >
      <template slot="prepend">
        <i :class="selectedIcon || 'el-icon-picture-outline'"></i>
      </template>
      <template slot="append">
        <el-button icon="el-icon-delete" @click.stop="clearIcon" v-if="selectedIcon"></el-button>
      </template>
    </el-input>
  </el-popover>
</template>

<script>
export default {
  name: 'IconSelector',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请选择图标'
    }
  },
  data() {
    return {
      visible: false,
      searchText: '',
      selectedIcon: this.value,
      // Element UI 常用图标列表
      icons: [
        'el-icon-platform-eleme',
        'el-icon-eleme',
        'el-icon-delete-solid',
        'el-icon-delete',
        'el-icon-s-tools',
        'el-icon-setting',
        'el-icon-user-solid',
        'el-icon-user',
        'el-icon-phone',
        'el-icon-phone-outline',
        'el-icon-more',
        'el-icon-more-outline',
        'el-icon-star-on',
        'el-icon-star-off',
        'el-icon-s-goods',
        'el-icon-goods',
        'el-icon-warning',
        'el-icon-warning-outline',
        'el-icon-question',
        'el-icon-info',
        'el-icon-remove',
        'el-icon-circle-plus',
        'el-icon-success',
        'el-icon-error',
        'el-icon-zoom-in',
        'el-icon-zoom-out',
        'el-icon-remove-outline',
        'el-icon-circle-plus-outline',
        'el-icon-circle-check',
        'el-icon-circle-close',
        'el-icon-s-help',
        'el-icon-help',
        'el-icon-minus',
        'el-icon-plus',
        'el-icon-check',
        'el-icon-close',
        'el-icon-picture',
        'el-icon-picture-outline',
        'el-icon-picture-outline-round',
        'el-icon-upload',
        'el-icon-upload2',
        'el-icon-download',
        'el-icon-camera-solid',
        'el-icon-camera',
        'el-icon-video-camera-solid',
        'el-icon-video-camera',
        'el-icon-message-solid',
        'el-icon-bell',
        'el-icon-s-cooperation',
        'el-icon-s-order',
        'el-icon-s-platform',
        'el-icon-s-fold',
        'el-icon-s-unfold',
        'el-icon-s-operation',
        'el-icon-s-promotion',
        'el-icon-s-home',
        'el-icon-s-release',
        'el-icon-s-ticket',
        'el-icon-s-management',
        'el-icon-s-open',
        'el-icon-s-shop',
        'el-icon-s-marketing',
        'el-icon-s-flag',
        'el-icon-s-comment',
        'el-icon-s-finance',
        'el-icon-s-claim',
        'el-icon-s-custom',
        'el-icon-s-opportunity',
        'el-icon-s-data',
        'el-icon-s-check',
        'el-icon-s-grid',
        'el-icon-menu',
        'el-icon-share',
        'el-icon-d-caret',
        'el-icon-caret-left',
        'el-icon-caret-right',
        'el-icon-caret-bottom',
        'el-icon-caret-top',
        'el-icon-bottom-left',
        'el-icon-bottom-right',
        'el-icon-back',
        'el-icon-right',
        'el-icon-bottom',
        'el-icon-top',
        'el-icon-top-left',
        'el-icon-top-right',
        'el-icon-arrow-left',
        'el-icon-arrow-right',
        'el-icon-arrow-down',
        'el-icon-arrow-up',
        'el-icon-d-arrow-left',
        'el-icon-d-arrow-right',
        'el-icon-video-pause',
        'el-icon-video-play',
        'el-icon-refresh',
        'el-icon-refresh-right',
        'el-icon-refresh-left',
        'el-icon-finished',
        'el-icon-sort',
        'el-icon-sort-up',
        'el-icon-sort-down',
        'el-icon-rank',
        'el-icon-loading',
        'el-icon-view',
        'el-icon-c-scale-to-original',
        'el-icon-date',
        'el-icon-edit',
        'el-icon-edit-outline',
        'el-icon-folder',
        'el-icon-folder-opened',
        'el-icon-folder-add',
        'el-icon-folder-remove',
        'el-icon-folder-delete',
        'el-icon-folder-checked',
        'el-icon-tickets',
        'el-icon-document-remove',
        'el-icon-document-delete',
        'el-icon-document-copy',
        'el-icon-document-checked',
        'el-icon-document',
        'el-icon-document-add',
        'el-icon-printer',
        'el-icon-paperclip',
        'el-icon-takeaway-box',
        'el-icon-search',
        'el-icon-monitor',
        'el-icon-attract',
        'el-icon-mobile',
        'el-icon-scissors',
        'el-icon-umbrella',
        'el-icon-headset',
        'el-icon-brush',
        'el-icon-mouse',
        'el-icon-coordinate',
        'el-icon-magic-stick',
        'el-icon-reading',
        'el-icon-data-line',
        'el-icon-data-board',
        'el-icon-pie-chart',
        'el-icon-data-analysis',
        'el-icon-collection-tag',
        'el-icon-film',
        'el-icon-suitcase',
        'el-icon-suitcase-1',
        'el-icon-receiving',
        'el-icon-collection',
        'el-icon-files',
        'el-icon-notebook-1',
        'el-icon-notebook-2',
        'el-icon-toilet-paper',
        'el-icon-office-building',
        'el-icon-school',
        'el-icon-table-lamp',
        'el-icon-house',
        'el-icon-no-smoking',
        'el-icon-smoking',
        'el-icon-shopping-cart-full',
        'el-icon-shopping-cart-1',
        'el-icon-shopping-cart-2',
        'el-icon-shopping-bag-1',
        'el-icon-shopping-bag-2',
        'el-icon-sold-out',
        'el-icon-sell',
        'el-icon-present',
        'el-icon-box',
        'el-icon-bank-card',
        'el-icon-money',
        'el-icon-coin',
        'el-icon-wallet',
        'el-icon-discount',
        'el-icon-price-tag',
        'el-icon-news',
        'el-icon-guide',
        'el-icon-male',
        'el-icon-female',
        'el-icon-thumb',
        'el-icon-cpu',
        'el-icon-link',
        'el-icon-connection',
        'el-icon-open',
        'el-icon-turn-off',
        'el-icon-set-up',
        'el-icon-chat-round',
        'el-icon-chat-line-round',
        'el-icon-chat-square',
        'el-icon-chat-dot-round',
        'el-icon-chat-dot-square',
        'el-icon-chat-line-square',
        'el-icon-message',
        'el-icon-postcard',
        'el-icon-position',
        'el-icon-turn-off-microphone',
        'el-icon-microphone',
        'el-icon-close-notification',
        'el-icon-bangzhu',
        'el-icon-time',
        'el-icon-odometer',
        'el-icon-crop',
        'el-icon-aim',
        'el-icon-switch-button',
        'el-icon-full-screen',
        'el-icon-copy-document',
        'el-icon-mic',
        'el-icon-stopwatch',
        'el-icon-medal-1',
        'el-icon-medal',
        'el-icon-trophy',
        'el-icon-trophy-1',
        'el-icon-first-aid-kit',
        'el-icon-discover',
        'el-icon-place',
        'el-icon-location',
        'el-icon-location-outline',
        'el-icon-location-information',
        'el-icon-add-location',
        'el-icon-delete-location',
        'el-icon-map-location',
        'el-icon-alarm-clock',
        'el-icon-timer',
        'el-icon-watch-1',
        'el-icon-watch',
        'el-icon-lock',
        'el-icon-unlock',
        'el-icon-key',
        'el-icon-service',
        'el-icon-mobile-phone',
        'el-icon-bicycle',
        'el-icon-truck',
        'el-icon-ship',
        'el-icon-basketball',
        'el-icon-football',
        'el-icon-soccer',
        'el-icon-baseball',
        'el-icon-wind-power',
        'el-icon-light-rain',
        'el-icon-lightning',
        'el-icon-heavy-rain',
        'el-icon-sunrise',
        'el-icon-sunrise-1',
        'el-icon-sunset',
        'el-icon-sunny',
        'el-icon-cloudy',
        'el-icon-partly-cloudy',
        'el-icon-cloudy-and-sunny',
        'el-icon-moon',
        'el-icon-moon-night',
        'el-icon-dish',
        'el-icon-dish-1',
        'el-icon-food',
        'el-icon-chicken',
        'el-icon-fork-spoon',
        'el-icon-knife-fork',
        'el-icon-burger',
        'el-icon-tableware',
        'el-icon-sugar',
        'el-icon-dessert',
        'el-icon-ice-cream',
        'el-icon-hot-water',
        'el-icon-water-cup',
        'el-icon-coffee-cup',
        'el-icon-cold-drink',
        'el-icon-goblet',
        'el-icon-goblet-full',
        'el-icon-goblet-square',
        'el-icon-goblet-square-full',
        'el-icon-refrigerator',
        'el-icon-grape',
        'el-icon-watermelon',
        'el-icon-cherry',
        'el-icon-apple',
        'el-icon-pear',
        'el-icon-orange',
        'el-icon-coffee',
        'el-icon-ice-tea',
        'el-icon-ice-drink',
        'el-icon-milk-tea',
        'el-icon-potato-strips',
        'el-icon-lollipop',
        'el-icon-ice-cream-square',
        'el-icon-ice-cream-round'
      ]
    }
  },
  computed: {
    filteredIcons() {
      if (!this.searchText) {
        return this.icons
      }
      return this.icons.filter(icon => 
        icon.toLowerCase().includes(this.searchText.toLowerCase())
      )
    },
    displayValue() {
      return this.selectedIcon || ''
    }
  },
  watch: {
    value(newVal) {
      this.selectedIcon = newVal
    }
  },
  methods: {
    selectIcon(icon) {
      this.selectedIcon = icon
      this.$emit('input', icon)
      this.$emit('change', icon)
      this.visible = false
    },
    clearIcon() {
      this.selectedIcon = ''
      this.$emit('input', '')
      this.$emit('change', '')
    }
  }
}
</script>

<style scoped>
.icon-selector {
  padding: 10px;
}

.search-input {
  margin-bottom: 10px;
}

.icon-list {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 8px;
  max-height: 300px;
  overflow-y: auto;
}

.icon-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 18px;
  color: #606266;
}

.icon-item:hover {
  border-color: #409eff;
  color: #409eff;
  background-color: #ecf5ff;
}

.icon-item.is-active {
  border-color: #409eff;
  color: #409eff;
  background-color: #ecf5ff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.empty-text {
  grid-column: 1 / -1;
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

/* 滚动条样式 */
.icon-list::-webkit-scrollbar {
  width: 6px;
}

.icon-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.icon-list::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.icon-list::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
